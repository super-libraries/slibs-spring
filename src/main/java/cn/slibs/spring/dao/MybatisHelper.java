package cn.slibs.spring.dao;

import com.iofairy.except.GeneralException;
import com.iofairy.id.TimedID;
import com.iofairy.falcon.iterable.CollectionKit;
import com.iofairy.lambda.PT2;
import com.iofairy.lambda.RT2;
import com.iofairy.tcf.Close;
import com.iofairy.top.G;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionHolder;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static com.iofairy.validator.Preconditions.*;

/**
 * Mybatis 工具类（<b>建议使用 {@code @Bean} 注入Spring容器中</b>）
 *
 * @since 0.0.1
 */
@Slf4j
@Getter
public class MybatisHelper {
    /** 默认每批次数量 */
    @Getter
    private static int defaultBatchSize = 1000;
    /** 默认用于判断更新或者插入SQL名称 */
    @Getter
    private static String defaultSelectSqlMethodName = "selectById";
    /** 默认更新的SQL名称 */
    @Getter
    private static String defaultInsertSqlMethodName = "insert";
    /** 默认插入的SQL名称 */
    @Getter
    private static String defaultUpdateSqlMethodName = "update";
    /** 默认输出日志的每批次数量 */
    @Getter
    private static int defaultPrintLogBatchSize = 1000;

    /*======================
     ******* 实例属性 *******
     ======================*/
    private final SqlSessionTemplate sqlSessionTemplate;
    /** 每批次数量 */
    private int batchSize;
    /** 用于判断更新或者插入SQL名称 */
    private String selectSqlMethodName;
    /** 更新的SQL名称 */
    private String insertSqlMethodName;
    /** 插入的SQL名称 */
    private String updateSqlMethodName;


    MybatisHelper(SqlSessionTemplate sqlSessionTemplate) {
        this.sqlSessionTemplate = sqlSessionTemplate;
    }

    public static MybatisHelper build(SqlSessionTemplate sqlSessionTemplate) {
        return build(defaultBatchSize, sqlSessionTemplate);
    }

    public static MybatisHelper build(int batchSize, SqlSessionTemplate sqlSessionTemplate) {
        return build(batchSize, sqlSessionTemplate, defaultSelectSqlMethodName, defaultInsertSqlMethodName, defaultUpdateSqlMethodName);
    }

    public static MybatisHelper build(int batchSize, SqlSessionTemplate sqlSessionTemplate, String selectSqlMethodName) {
        return build(batchSize, sqlSessionTemplate, selectSqlMethodName, defaultInsertSqlMethodName, defaultUpdateSqlMethodName);
    }

    public static MybatisHelper build(int batchSize,
                                      SqlSessionTemplate sqlSessionTemplate,
                                      String selectSqlMethodName,
                                      String insertSqlMethodName) {
        return build(batchSize, sqlSessionTemplate, selectSqlMethodName, insertSqlMethodName, defaultUpdateSqlMethodName);
    }

    public static MybatisHelper build(int batchSize,
                                      SqlSessionTemplate sqlSessionTemplate,
                                      String selectSqlMethodName,
                                      String insertSqlMethodName,
                                      String updateSqlMethodName) {
        checkNullNPE(sqlSessionTemplate, args("sqlSessionTemplate"));
        checkHasBlank(args(selectSqlMethodName, insertSqlMethodName, updateSqlMethodName), args("selectSqlMethodName", "insertSqlMethodName", "updateSqlMethodName"));

        return new MybatisHelper(sqlSessionTemplate)
                .setBatchSize(batchSize)
                .setSelectSqlMethodName(selectSqlMethodName)
                .setInsertSqlMethodName(insertSqlMethodName)
                .setUpdateSqlMethodName(updateSqlMethodName)
                ;
    }


    /*===================================================================
     ********************************************************************
     ====================================================================
     ******************   批量更新或批量插入（只能选择其一）  ******************
     ====================================================================
     ********************************************************************
     ===================================================================*/

    /**
     * 批量更新、插入、删除
     *
     * @param mapperClass  mapper class
     * @param models       需要更新或插入的实体类
     * @param batchSize    每批次数量
     * @param updateAction 更新操作，如：（{@code (mapper, model) -> mapper.insert(model)}）
     * @param <MAPPER>     mapper类型
     * @param <MODEL>      实体类型
     */
    public <MAPPER, MODEL> void batchUpdate(Class<MAPPER> mapperClass,
                                            Collection<MODEL> models,
                                            int batchSize,
                                            RT2<MAPPER, MODEL, Integer, Exception> updateAction) {
        batchUpdate(this.sqlSessionTemplate, mapperClass, models, batchSize, updateAction);
    }

    /**
     * 批量更新、插入、删除
     *
     * @param mapperClass  mapper class
     * @param models       需要更新或插入的实体类
     * @param updateAction 更新操作，如：（{@code (mapper, model) -> mapper.insert(model)}）
     * @param <MAPPER>     mapper类型
     * @param <MODEL>      实体类型
     */
    public <MAPPER, MODEL> void batchUpdate(Class<MAPPER> mapperClass,
                                            Collection<MODEL> models,
                                            RT2<MAPPER, MODEL, Integer, Exception> updateAction) {
        batchUpdate(this.sqlSessionTemplate, mapperClass, models, this.batchSize, updateAction);
    }

    /*==================================================
     ******************   静态方法调用  ******************
     ==================================================*/

    /**
     * 批量更新、插入、删除
     *
     * @param sqlSessionTemplate sqlSessionTemplate
     * @param mapperClass        mapper class
     * @param models             需要更新或插入的实体类
     * @param updateAction       更新操作，如：（{@code (mapper, model) -> mapper.insert(model)}）
     * @param <MAPPER>           mapper类型
     * @param <MODEL>            实体类型
     */
    public static <MAPPER, MODEL> void batchUpdate(SqlSessionTemplate sqlSessionTemplate,
                                                   Class<MAPPER> mapperClass,
                                                   Collection<MODEL> models,
                                                   RT2<MAPPER, MODEL, Integer, Exception> updateAction) {
        batchUpdate(sqlSessionTemplate, mapperClass, models, defaultBatchSize, updateAction);
    }

    /**
     * 批量更新、插入、删除
     *
     * @param sqlSessionTemplate sqlSessionTemplate
     * @param mapperClass        mapper class
     * @param models             需要更新或插入的实体类
     * @param batchSize          每批次数量
     * @param updateAction       更新操作，如：（{@code (mapper, model) -> mapper.insert(model)}）
     * @param <MAPPER>           mapper类型
     * @param <MODEL>            实体类型
     */
    public static <MAPPER, MODEL> void batchUpdate(SqlSessionTemplate sqlSessionTemplate,
                                                   Class<MAPPER> mapperClass,
                                                   Collection<MODEL> models,
                                                   int batchSize,
                                                   RT2<MAPPER, MODEL, Integer, Exception> updateAction) {
        checkHasNullNPE(args(sqlSessionTemplate, mapperClass, updateAction), args("sqlSessionTemplate", "mapperClass", "updateAction"));
        checkEmpty(models, args("models"));
        checkArgument(defaultBatchSize < 1, "批量大小必须大于0！");

        SqlSession sqlSession = getSqlSession(sqlSessionTemplate.getSqlSessionFactory());

        final String logId = TimedID.getId();
        long processedCount = 0;
        long printLogCount = 0;
        try {
            MAPPER mapper = sqlSession.getMapper(mapperClass);
            List<List<MODEL>> modelLists = CollectionKit.divide(new ArrayList<>(models), batchSize);
            for (List<MODEL> modelList : modelLists) {
                for (MODEL model : modelList) {
                    updateAction.$(mapper, model);
                }
                sqlSession.flushStatements();
                sqlSession.clearCache();

                processedCount += modelList.size();
                printLogCount += modelList.size();
                if (printLogCount >= defaultPrintLogBatchSize) {
                    printLogCount = 0;
                    log.debug("batchUpdate_logId：{}，mapperClass：{}，正在处理……已处理数据量：{}", logId, mapperClass.getSimpleName(), processedCount);
                }
            }

            log.debug("batchUpdate_logId：{}，mapperClass：{}，处理完成。处理数据量：{}", logId, mapperClass.getSimpleName(), processedCount);
            sqlSession.commit();
        } catch (Throwable e) {
            log.debug("batchUpdate_logId：{}，mapperClass：{}，处理失败，失败原因：{}，已处理数据量：{}", logId, mapperClass.getSimpleName(), e.getMessage(), processedCount);
            if (sqlSession != null) {
                sqlSession.rollback();
            }
            throw new GeneralException(e, "[${mapperName}]批量更新（或插入）异常！", mapperClass.getSimpleName());
        } finally {
            Close.close(sqlSession);
        }

    }


    /*======================================================================
     ***********************************************************************
     =======================================================================
     ******************   批量更新或插入（有则更新，无则插入）  ******************
     =======================================================================
     ***********************************************************************
     ======================================================================*/

    /**
     * 批量更新或插入（不存在即插入，存在则更新）
     *
     * @param mapperClass mapper class
     * @param models      需要更新或插入的数据
     * @param <MAPPER>    mapper类型
     * @param <MODEL>     实体类型
     */
    public <MAPPER, MODEL> void batchSaveOrUpdate(Class<MAPPER> mapperClass,
                                                  Collection<MODEL> models) {
        batchSaveOrUpdate(this.sqlSessionTemplate, mapperClass, models, this.batchSize, this.selectSqlMethodName, this.insertSqlMethodName, this.updateSqlMethodName);
    }

    /**
     * 批量更新或插入（不存在即插入，存在则更新）
     *
     * @param mapperClass mapper class
     * @param models      需要更新或插入的数据
     * @param batchSize   每批次数量
     * @param <MAPPER>    mapper类型
     * @param <MODEL>     实体类型
     */
    public <MAPPER, MODEL> void batchSaveOrUpdate(Class<MAPPER> mapperClass,
                                                  Collection<MODEL> models,
                                                  int batchSize) {
        batchSaveOrUpdate(this.sqlSessionTemplate, mapperClass, models, batchSize, this.selectSqlMethodName, this.insertSqlMethodName, this.updateSqlMethodName);
    }

    /**
     * 批量更新或插入（不存在即插入，存在则更新）
     *
     * @param mapperClass            mapper class
     * @param models                 需要更新或插入的数据
     * @param performInsertCondition 执行插入的条件，{@code true}则执行插入，{@code false}则执行更新，如：（{@code (mapper, model) -> mapper.selectById(model) == null }）
     * @param <MAPPER>               mapper类型
     * @param <MODEL>                实体类型
     */
    public <MAPPER, MODEL> void batchSaveOrUpdate(Class<MAPPER> mapperClass,
                                                  Collection<MODEL> models,
                                                  PT2<MAPPER, MODEL, Exception> performInsertCondition) {
        batchSaveOrUpdate(this.sqlSessionTemplate, mapperClass, models, this.batchSize, performInsertCondition, this.insertSqlMethodName, this.updateSqlMethodName);
    }

    /**
     * 批量更新或插入（不存在即插入，存在则更新）
     *
     * @param mapperClass            mapper class
     * @param models                 需要更新或插入的数据
     * @param batchSize              每批次数量
     * @param performInsertCondition 执行插入的条件，{@code true}则执行插入，{@code false}则执行更新，如：（{@code (mapper, model) -> mapper.selectById(model) == null }）
     * @param insertSqlMethodName    插入SQL方法名
     * @param updateSqlMethodName    更新SQL方法名
     * @param <MAPPER>               mapper类型
     * @param <MODEL>                实体类型
     */
    public <MAPPER, MODEL> void batchSaveOrUpdate(Class<MAPPER> mapperClass,
                                                  Collection<MODEL> models,
                                                  int batchSize,
                                                  PT2<MAPPER, MODEL, Exception> performInsertCondition,
                                                  String insertSqlMethodName,
                                                  String updateSqlMethodName) {
        batchSaveOrUpdate(this.sqlSessionTemplate, mapperClass, models, batchSize, performInsertCondition, insertSqlMethodName, updateSqlMethodName);
    }

    /**
     * 批量更新或插入（不存在即插入，存在则更新）
     *
     * @param mapperClass         mapper class
     * @param models              需要更新或插入的数据
     * @param batchSize           每批次数量
     * @param selectSqlMethodName 按条件查询的SQL方法名
     * @param insertSqlMethodName 插入SQL方法名
     * @param updateSqlMethodName 更新SQL方法名
     * @param <MAPPER>            mapper类型
     * @param <MODEL>             实体类型
     */
    public <MAPPER, MODEL> void batchSaveOrUpdate(Class<MAPPER> mapperClass,
                                                  Collection<MODEL> models,
                                                  int batchSize,
                                                  String selectSqlMethodName,
                                                  String insertSqlMethodName,
                                                  String updateSqlMethodName) {
        batchSaveOrUpdate(this.sqlSessionTemplate, mapperClass, models, batchSize, selectSqlMethodName, insertSqlMethodName, updateSqlMethodName);
    }
    /*==================================================
     ******************   静态方法调用  ******************
     ==================================================*/

    /**
     * 批量更新或插入（不存在即插入，存在则更新）
     *
     * @param sqlSessionTemplate sqlSessionTemplate
     * @param mapperClass        mapper class
     * @param models             需要更新或插入的数据
     * @param <MAPPER>           mapper类型
     * @param <MODEL>            实体类型
     */
    public static <MAPPER, MODEL> void batchSaveOrUpdate(SqlSessionTemplate sqlSessionTemplate,
                                                         Class<MAPPER> mapperClass,
                                                         Collection<MODEL> models) {
        batchSaveOrUpdate(sqlSessionTemplate, mapperClass, models, defaultBatchSize, defaultSelectSqlMethodName, defaultInsertSqlMethodName, defaultUpdateSqlMethodName);
    }


    /**
     * 批量更新或插入（不存在即插入，存在则更新）
     *
     * @param sqlSessionTemplate     sqlSessionTemplate
     * @param mapperClass            mapper class
     * @param models                 需要更新或插入的数据
     * @param performInsertCondition 执行插入的条件，{@code true}则执行插入，{@code false}则执行更新，如：（{@code (mapper, model) -> mapper.selectById(model) == null }）
     * @param <MAPPER>               mapper类型
     * @param <MODEL>                实体类型
     */
    public static <MAPPER, MODEL> void batchSaveOrUpdate(SqlSessionTemplate sqlSessionTemplate,
                                                         Class<MAPPER> mapperClass,
                                                         Collection<MODEL> models,
                                                         PT2<MAPPER, MODEL, Exception> performInsertCondition) {
        batchSaveOrUpdate(sqlSessionTemplate, mapperClass, models, defaultBatchSize, performInsertCondition, defaultInsertSqlMethodName, defaultUpdateSqlMethodName);
    }


    /**
     * 批量更新或插入（不存在即插入，存在则更新）
     *
     * @param sqlSessionTemplate     sqlSessionTemplate
     * @param mapperClass            mapper class
     * @param models                 需要更新或插入的数据
     * @param batchSize              每批次数量
     * @param performInsertCondition 执行插入的条件，{@code true}则执行插入，{@code false}则执行更新，如：（{@code (mapper, model) -> mapper.selectById(model) == null }）
     * @param insertSqlMethodName    插入SQL方法名
     * @param updateSqlMethodName    更新SQL方法名
     * @param <MAPPER>               mapper类型
     * @param <MODEL>                实体类型
     */
    public static <MAPPER, MODEL> void batchSaveOrUpdate(SqlSessionTemplate sqlSessionTemplate,
                                                         Class<MAPPER> mapperClass,
                                                         Collection<MODEL> models,
                                                         int batchSize,
                                                         PT2<MAPPER, MODEL, Exception> performInsertCondition,
                                                         String insertSqlMethodName,
                                                         String updateSqlMethodName) {
        checkEmpty(models, args("models"));
        checkArgument(defaultBatchSize < 1, "批量大小必须大于0！");
        checkHasBlank(args(insertSqlMethodName, updateSqlMethodName), args("insertSqlMethodName", "updateSqlMethodName"));
        checkHasNullNPE(args(sqlSessionTemplate, mapperClass, performInsertCondition), args("sqlSessionTemplate", "mapperClass", "performInsertCondition"));

        _batchSaveOrUpdate(sqlSessionTemplate, mapperClass, models, batchSize, performInsertCondition, null, insertSqlMethodName, updateSqlMethodName);
    }

    /**
     * 批量更新或插入（不存在即插入，存在则更新）
     *
     * @param sqlSessionTemplate  sqlSessionTemplate
     * @param mapperClass         mapper class
     * @param models              需要更新或插入的数据
     * @param batchSize           每批次数量
     * @param selectSqlMethodName 按条件查询的SQL方法名
     * @param insertSqlMethodName 插入SQL方法名
     * @param updateSqlMethodName 更新SQL方法名
     * @param <MAPPER>            mapper类型
     * @param <MODEL>             实体类型
     */
    public static <MAPPER, MODEL> void batchSaveOrUpdate(SqlSessionTemplate sqlSessionTemplate,
                                                         Class<MAPPER> mapperClass,
                                                         Collection<MODEL> models,
                                                         int batchSize,
                                                         String selectSqlMethodName,
                                                         String insertSqlMethodName,
                                                         String updateSqlMethodName) {
        checkEmpty(models, args("models"));
        checkArgument(defaultBatchSize < 1, "批量大小必须大于0！");
        checkHasNullNPE(args(sqlSessionTemplate, mapperClass), args("sqlSessionTemplate", "mapperClass"));
        checkHasBlank(args(selectSqlMethodName, insertSqlMethodName, updateSqlMethodName), args("selectSqlMethodName", "insertSqlMethodName", "updateSqlMethodName"));

        _batchSaveOrUpdate(sqlSessionTemplate, mapperClass, models, batchSize, null, selectSqlMethodName, insertSqlMethodName, updateSqlMethodName);
    }


    private static <MAPPER, MODEL> void _batchSaveOrUpdate(SqlSessionTemplate sqlSessionTemplate,
                                                           Class<MAPPER> mapperClass,
                                                           Collection<MODEL> models,
                                                           int batchSize,
                                                           PT2<MAPPER, MODEL, Exception> performInsertCondition,
                                                           String selectSqlMethodName,
                                                           String insertSqlMethodName,
                                                           String updateSqlMethodName) {
        final String selectSql = mapperClass.getName() + "." + selectSqlMethodName;
        final String updateSql = mapperClass.getName() + "." + updateSqlMethodName;
        final String insertSql = mapperClass.getName() + "." + insertSqlMethodName;
        final SqlSession sqlSession = getSqlSession(sqlSessionTemplate.getSqlSessionFactory());

        PT2<MAPPER, MODEL, Exception> insertCondition = performInsertCondition == null ?
                (mapper, model) -> G.isEmpty(sqlSession.selectList(selectSql, model))
                : performInsertCondition;

        final String logId = TimedID.getId();
        long processedCount = 0;
        long printLogCount = 0;
        try {
            MAPPER mapper = sqlSession.getMapper(mapperClass);
            List<List<MODEL>> modelLists = CollectionKit.divide(new ArrayList<>(models), batchSize);
            for (List<MODEL> modelList : modelLists) {
                for (MODEL model : modelList) {
                    boolean notExists = insertCondition.$(mapper, model);
                    if (notExists) {
                        sqlSession.insert(insertSql, model);
                    } else {
                        sqlSession.update(updateSql, model);
                    }
                }
                sqlSession.flushStatements();
                sqlSession.clearCache();

                processedCount += modelList.size();
                printLogCount += modelList.size();
                if (printLogCount >= defaultPrintLogBatchSize) {
                    printLogCount = 0;
                    log.debug("batchSaveOrUpdate_logId：{}，mapperClass：{}，正在处理……已处理数据量：{}", logId, mapperClass.getSimpleName(), processedCount);
                }
            }
            log.debug("batchSaveOrUpdate_logId：{}，mapperClass：{}，处理完成。处理数据量：{}", logId, mapperClass.getSimpleName(), processedCount);

            sqlSession.commit();
        } catch (Throwable e) {
            log.debug("batchSaveOrUpdate_logId：{}，mapperClass：{}，处理失败，失败原因：{}，已处理数据量：{}", logId, mapperClass.getSimpleName(), e.getMessage(), processedCount);
            if (sqlSession != null) {
                sqlSession.rollback();
            }
            throw new GeneralException(e, "[${mapperName}]批量更新（或插入）异常！", mapperClass.getSimpleName());
        } finally {
            Close.close(sqlSession);
        }
    }

    private static SqlSession getSqlSession(SqlSessionFactory sqlSessionFactory) {
        boolean transaction = false;
        try {
            SqlSessionHolder sqlSessionHolder = (SqlSessionHolder) TransactionSynchronizationManager.getResource(sqlSessionFactory);
            transaction = TransactionSynchronizationManager.isSynchronizationActive();
            if (sqlSessionHolder != null) {
                SqlSession sqlSession = sqlSessionHolder.getSqlSession();
                // 原生无法支持执行器切换，当存在批量操作时，会嵌套两个session的，优先commit上一个session
                // 按道理来说，这里的值应该一直为false。
                sqlSession.commit(!transaction);
            }
        } catch (Throwable e) {

        }

        SqlSession sqlSession = sqlSessionFactory.openSession(ExecutorType.BATCH, false);
        if (!transaction) {
            log.warn("SqlSession [" + sqlSession + "] Transaction not enabled");
        }
        return sqlSession;
    }



    /*========================
     ******* setter方法 *******
     ========================*/

    public MybatisHelper setBatchSize(int batchSize) {
        checkArgument(defaultBatchSize < 1, "批量大小必须大于0！");
        this.batchSize = batchSize;
        return this;
    }

    public MybatisHelper setSelectSqlMethodName(String selectSqlMethodName) {
        checkBlank(selectSqlMethodName, args("selectSqlMethodName"));
        this.selectSqlMethodName = selectSqlMethodName;
        return this;
    }

    public MybatisHelper setInsertSqlMethodName(String insertSqlMethodName) {
        checkBlank(insertSqlMethodName, args("insertSqlMethodName"));
        this.insertSqlMethodName = insertSqlMethodName;
        return this;
    }

    public MybatisHelper setUpdateSqlMethodName(String updateSqlMethodName) {
        checkBlank(updateSqlMethodName, args("updateSqlMethodName"));
        this.updateSqlMethodName = updateSqlMethodName;
        return this;
    }

    public static void setDefaultBatchSize(int defaultBatchSize) {
        checkArgument(defaultBatchSize < 1, "批量大小必须大于0！");
        MybatisHelper.defaultBatchSize = defaultBatchSize;
    }

    public static void setDefaultSelectSqlMethodName(String defaultSelectSqlMethodName) {
        checkBlank(defaultSelectSqlMethodName, args("defaultSelectSqlMethodName"));
        MybatisHelper.defaultSelectSqlMethodName = defaultSelectSqlMethodName;
    }

    public static void setDefaultUpdateSqlMethodName(String defaultUpdateSqlMethodName) {
        checkBlank(defaultUpdateSqlMethodName, args("defaultUpdateSqlMethodName"));
        MybatisHelper.defaultUpdateSqlMethodName = defaultUpdateSqlMethodName;
    }

    public static void setDefaultInsertSqlMethodName(String defaultInsertSqlMethodName) {
        checkBlank(defaultInsertSqlMethodName, args("defaultInsertSqlMethodName"));
        MybatisHelper.defaultInsertSqlMethodName = defaultInsertSqlMethodName;
    }

    public static void setDefaultPrintLogBatchSize(int defaultPrintLogBatchSize) {
        checkArgument(defaultPrintLogBatchSize < 1, "批量大小必须大于0！");
        MybatisHelper.defaultPrintLogBatchSize = defaultPrintLogBatchSize;
    }

    @Override
    public String toString() {
        return "MybatisHelper{" +
                "batchSize=" + batchSize +
                ", selectSqlMethodName='" + selectSqlMethodName + '\'' +
                ", updateSqlMethodName='" + updateSqlMethodName + '\'' +
                ", insertSqlMethodName='" + insertSqlMethodName + '\'' +
                '}';
    }

}
