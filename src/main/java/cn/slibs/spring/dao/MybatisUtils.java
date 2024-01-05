package cn.slibs.spring.dao;

import com.iofairy.falcon.iterable.CollectionKit;
import com.iofairy.lambda.RT2;
import com.iofairy.tcf.Close;
import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSession;
import org.mybatis.spring.SqlSessionTemplate;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * mybatis 工具类
 *
 * @since 0.0.1
 */
public class MybatisUtils {

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
    public static <MAPPER, MODEL> int batchUpdate(SqlSessionTemplate sqlSessionTemplate,
                                                  Class<MAPPER> mapperClass,
                                                  Collection<MODEL> models,
                                                  int batchSize,
                                                  RT2<MAPPER, MODEL, Integer, Exception> updateAction) {
        List<List<MODEL>> modelLists = CollectionKit.divide(new ArrayList<>(models), batchSize);
        for (List<MODEL> modelList : modelLists) {
            batchUpdate(sqlSessionTemplate, mapperClass, modelList, updateAction);
        }
        return models.size();
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
    public static <MAPPER, MODEL> int batchUpdate(SqlSessionTemplate sqlSessionTemplate,
                                                  Class<MAPPER> mapperClass,
                                                  List<MODEL> models,
                                                  RT2<MAPPER, MODEL, Integer, Exception> updateAction) {
        SqlSession sqlSession = null;
        try {
            sqlSession = sqlSessionTemplate.getSqlSessionFactory().openSession(ExecutorType.BATCH, false);
            MAPPER mapper = sqlSession.getMapper(mapperClass);
            for (MODEL model : models) {
                updateAction.$(mapper, model);
            }

            sqlSession.commit();
            sqlSession.clearCache();
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            Close.close(sqlSession);
        }
        return models.size();
    }

}
