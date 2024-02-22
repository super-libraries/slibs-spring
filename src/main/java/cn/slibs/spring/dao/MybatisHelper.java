package cn.slibs.spring.dao;

import com.iofairy.falcon.iterable.CollectionKit;
import com.iofairy.lambda.RT2;
import com.iofairy.tcf.Close;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSession;
import org.mybatis.spring.SqlSessionTemplate;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * mybatis 工具
 *
 * @since 0.0.1
 */
@Getter
@Setter
public class MybatisHelper {
    /**
     * 默认批量更新大小
     */
    public static int defaultBatchSize = 1000;

    @Accessors(chain = true)
    private int batchSize;
    @Accessors(chain = true)
    private SqlSessionTemplate sqlSessionTemplate;

    MybatisHelper() {
    }

    public static MybatisHelper build(SqlSessionTemplate sqlSessionTemplate) {
        return build(defaultBatchSize, sqlSessionTemplate);
    }

    public static MybatisHelper build(int batchSize, SqlSessionTemplate sqlSessionTemplate) {
        return new MybatisHelper()
                .setBatchSize(batchSize)
                .setSqlSessionTemplate(sqlSessionTemplate);
    }

    /**
     * 批量更新、插入、删除
     *
     * @param mapperClass  mapper class
     * @param models       需要更新或插入的实体类
     * @param batchSize    每批次数量
     * @param updateAction 更新操作，如：（{@code (mapper, model) -> mapper.insert(model)}）
     * @param <MAPPER>     mapper类型
     * @param <MODEL>      实体类型
     * @return 更新条数
     */
    public <MAPPER, MODEL> long batchUpdate(Class<MAPPER> mapperClass,
                                            Collection<MODEL> models,
                                            int batchSize,
                                            RT2<MAPPER, MODEL, Integer, Exception> updateAction) {
        return batchUpdate(sqlSessionTemplate, mapperClass, models, batchSize, updateAction);
    }

    /**
     * 批量更新、插入、删除
     *
     * @param mapperClass  mapper class
     * @param models       需要更新或插入的实体类
     * @param updateAction 更新操作，如：（{@code (mapper, model) -> mapper.insert(model)}）
     * @param <MAPPER>     mapper类型
     * @param <MODEL>      实体类型
     * @return 更新条数
     */
    public <MAPPER, MODEL> long batchUpdate(Class<MAPPER> mapperClass,
                                            Collection<MODEL> models,
                                            RT2<MAPPER, MODEL, Integer, Exception> updateAction) {
        return batchUpdate(sqlSessionTemplate, mapperClass, models, this.batchSize, updateAction);
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
     * @param batchSize          每批次数量
     * @param updateAction       更新操作，如：（{@code (mapper, model) -> mapper.insert(model)}）
     * @param <MAPPER>           mapper类型
     * @param <MODEL>            实体类型
     * @return 更新条数
     */
    public static <MAPPER, MODEL> long batchUpdate(SqlSessionTemplate sqlSessionTemplate,
                                                   Class<MAPPER> mapperClass,
                                                   Collection<MODEL> models,
                                                   int batchSize,
                                                   RT2<MAPPER, MODEL, Integer, Exception> updateAction) {
        long sumUpdateCount = 0L;
        List<List<MODEL>> modelLists = CollectionKit.divide(new ArrayList<>(models), batchSize);
        for (List<MODEL> modelList : modelLists) {
            sumUpdateCount += _batchUpdate(sqlSessionTemplate, mapperClass, modelList, updateAction);
        }
        return sumUpdateCount;
    }

    /**
     * 批量更新、插入、删除
     *
     * @param sqlSessionTemplate sqlSessionTemplate
     * @param mapperClass        mapper class
     * @param models             需要更新或插入的实体类
     * @param updateAction       更新操作，如：（{@code (mapper, model) -> mapper.insert(model)}）
     * @param <MAPPER>           mapper类型
     * @param <MODEL>            实体类型
     * @return 更新条数
     */
    public static <MAPPER, MODEL> long batchUpdate(SqlSessionTemplate sqlSessionTemplate,
                                                   Class<MAPPER> mapperClass,
                                                   Collection<MODEL> models,
                                                   RT2<MAPPER, MODEL, Integer, Exception> updateAction) {
        return batchUpdate(sqlSessionTemplate, mapperClass, models, defaultBatchSize, updateAction);
    }

    /**
     * 批量更新、插入、删除
     *
     * @param sqlSessionTemplate sqlSessionTemplate
     * @param mapperClass        mapper class
     * @param models             需要更新或插入的实体类
     * @param updateAction       更新操作
     * @param <MAPPER>           mapper类型
     * @param <MODEL>            实体类型
     * @return 更新条数
     */
    private static <MAPPER, MODEL> long _batchUpdate(SqlSessionTemplate sqlSessionTemplate,
                                                     Class<MAPPER> mapperClass,
                                                     List<MODEL> models,
                                                     RT2<MAPPER, MODEL, Integer, Exception> updateAction) {
        SqlSession sqlSession = null;
        try {
            sqlSession = sqlSessionTemplate.getSqlSessionFactory().openSession(ExecutorType.BATCH, false);
            MAPPER mapper = sqlSession.getMapper(mapperClass);
            for (MODEL model : models) {
                // sumUpdateCount += updateAction.$(mapper, model);     // 这种方式获取更新条数不准
                updateAction.$(mapper, model);
            }

            sqlSession.commit();
            sqlSession.clearCache();
        } catch (Exception e) {
            if (sqlSession != null) {
                sqlSession.rollback();
            }
            throw new RuntimeException("[" + mapperClass.getSimpleName() + "]批量更新（或插入）异常！", e);
        } finally {
            Close.close(sqlSession);
        }
        return models.size();
    }

    @Override
    public String toString() {
        return "MybatisHelper{" +
                "batchSize=" + batchSize +
                '}';
    }
}
