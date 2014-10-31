package com.travelsky.dao.impl;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;

import com.travelsky.dao.IBaseDao;
import com.travelsky.domain.Config;
import com.travelsky.framework.ConfigController;
import com.travelsky.framework.ManagerFactory;

/**
 * @author zyzhou
 * 
 * BaseDAO
 */
/**
 * @author zyzhou
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class BaseDaoImpl implements IBaseDao{
	
	
	private static final Log log = LogFactory.getLog(BaseDaoImpl.class);
	
    protected JdbcTemplate jdbcTemplate;

    protected final Log logger = LogFactory.getLog(getClass());
    
    
    public void setJdbcTemplate(JdbcTemplate template) {
        jdbcTemplate = template;
    }

    public JdbcTemplate getJdbcTemplate() {
    	
        return jdbcTemplate;
    }

    /**
     * 功能：判断数据集中是否包含特定列名 
     * 输入：数据集、列名 
     * 输出：true | false 
     * 备注：
     */
    public boolean hasColumn(ResultSet rs, String clmName) throws SQLException {
        ResultSetMetaData md = rs.getMetaData();
        for (int i = 1; i < md.getColumnCount() + 1; i++) {
            if (md.getColumnName(i).equalsIgnoreCase(clmName))
                return true;
        }
        return false;
    }

    /**
     * 功能：判断HashMap中是否包含特定列名从数据集得到某列的整型值 
     * 输入：数据集、列名 
     * 输出：true | false 
     * 备注：
     */
    public boolean hasColumn(Map allColumns, String clmName) {
        if (allColumns.containsKey(clmName.toUpperCase()))
            return true;
        return false;
    }

    /**
     * 功能:分页查询 
     * 输入： 完整SQL Mapper类 起始值(从1开始) 长度 
     * 输出：List
     * 说明：如果RowMapper继承自BaseRowMapper，则传递时必须是new XxxRowMapper,否则引起同步问题
     */
    public List pageQuery(String baseSql, RowMapper rm, int begin, int length) {
        String sql = "select * from (select rownum as my_rownum,table_a.* from("
                + baseSql
                + ") table_a where rownum<"
                + (begin + length)
                + ") where my_rownum>=" + begin;
        return getJdbcTemplate().query(sql, (rm));
    }

    /**
     * 功能:分页查询 
     * 输入： SQL SQL中的参数(即PreparedStatement中需要设置的参数) Mapper类 起始值(从1开始) 长度
     * 输出：List 
     * 说明：spring根据参数中对象的类型匹配到SQL中对应类型
     * 如果RowMapper继承自BaseRowMapper，则传递时必须是new XxxRowMapper,否则引起同步问题
     */
    public List pageQuery(String baseSql, Object[] params, RowMapper rm,
            int begin, int length) {
        String sql = "select * from (select rownum as my_rownum,table_a.* from("
                + baseSql
                + ") table_a where rownum<"
                + (begin + length)
                + ") where my_rownum>=" + begin;
        return getJdbcTemplate().query(sql, params,
               (rm));
    }

    /**
     * 功能:分页查询 
     * 输入： SQL SQL中的参数(即PreparedStatement中需要设置的参数) 参数中对象的类型映射到SQL中的类型
     * Mapper类 起始值(从1开始) 长度 
     * 输出：List 
     * 说明：如果RowMapper继承自BaseRowMapper，则传递时必须是new
     * XxxRowMapper,否则引起同步问题
     */

    public List pageQuery(String baseSql, Object[] params, int[] types,
            RowMapper rm, int begin, int length) {
        String sql = "select * from (select rownum as my_rownum,table_a.* from("
                + baseSql
                + ") table_a where rownum<"
                + (begin + length)
                + ") where my_rownum>=" + begin;
        return getJdbcTemplate().query(sql, params, types,
                (rm));
    }

    /**
     * 功能:查询 
     * 输入： SQL Mapper类 
     * 输出：List 
     * 说明：如果RowMapper继承自BaseRowMapper，则传递时必须是new
     * XxxRowMapper,否则引起同步问题
     */

    public List query(String sql, RowMapper rm) {
    	if(!sql.contains("MSI_CONFIG")){
	    	if(ManagerFactory.getConfigController().getPropertyAsInteger("ALL", Config.IS_PRINT_SQL)==1){
				log.info(sql	);
			}
    	}
        try {
			return getJdbcTemplate().query(sql, (rm));
		} catch (DataAccessException e) {
			e.printStackTrace();
		}
		return null;
    }

    /**
     * 功能:查询 
     * 输入： SQL SQL中的参数(即PreparedStatement中需要设置的参数) Mapper类 
     * 输出：List
     * 说明：如果RowMapper继承自BaseRowMapper，则传递时必须是new XxxRowMapper,否则引起同步问题
     */

    public List query(String sql, Object[] params, RowMapper rm) {
        try {
			return getJdbcTemplate().query(sql, params,
			        (rm));
		} catch (DataAccessException e) {
			e.printStackTrace();
		}
		return null;
    }

    /**
     * 功能:查询 
     * 输入： SQL SQL中的参数(即PreparedStatement中需要设置的参数) 参数中对象的类型映射到SQL中的类型
     * Mapper类 
     * 输出：List 
     * 说明：如果RowMapper继承自BaseRowMapper，则传递时必须是new
     * XxxRowMapper,否则引起同步问题
     */

    public List query(String sql, Object[] params, int[] types, RowMapper rm) {
    	if(ManagerFactory.getConfigController().getPropertyAsInteger("ALL", Config.IS_PRINT_SQL)==1){
			log.info(sql	);
		}
        try {
			return getJdbcTemplate().query(sql, params, types,rm);
		} catch (DataAccessException e) {
			e.printStackTrace();
		}
		return null;
    }

    /**
     * 功能:增加、删除、修改 
     * 输入： SQL SQL中的参数(即PreparedStatement中需要设置的参数)
     * 参数中对象的类型映射到SQL中的类型 
     * 输出：int 说明：
     */
    public int update(String sql, Object[] params, int[] types) {
    	if(ManagerFactory.getConfigController().getPropertyAsInteger("ALL", Config.IS_PRINT_SQL)==1){
			log.info(sql	);
		}
        return getJdbcTemplate().update(sql, params, types);
    }

    /**
     * 功能:批量更新 
     * 输入： SQL BatchPreparedStatementSetter 
     * 输出：int[]
     * 说明：回调setter的setValues方法
     */
    public int[] batchUpdate(String sql, BatchPreparedStatementSetter setter) {
        return getJdbcTemplate().batchUpdate(sql, setter);
    }

    /**
     * 功能:执行SQL 
     * 输入： SQL 
     * 输出：void 
     * 说明：
     */
    public void execute(String sql) {
        try {
        	//过滤第一次map中没有的时候会进行循环的情况!
        		if(ManagerFactory.getConfigController().getPropertyAsInteger("ALL", Config.IS_PRINT_SQL)==1){
        			log.info(sql	);
        	}
			getJdbcTemplate().execute(sql);
		} catch (DataAccessException e) {
			e.printStackTrace();
		}
    }

   /* public String getNextId(String sequenceName){
        String sql = "Select huet_seq.NextVal From Dual";
		int i=getJdbcTemplate().queryForInt(sql);
		return String.valueOf(i);
    }
 */ 
    /**
     * 获得下一个seq
     */
    public final String getSingleNextId(String sequenceName) {
        StringBuffer buf = new StringBuffer();
         buf.append("select ");
         buf.append(sequenceName);
         buf.append(".nextval as id from dual");
         try
         {
             String id = (String) this.getJdbcTemplate().query(buf.toString(),

                 new ResultSetExtractor() {
                     public Object extractData(ResultSet rs) {
                         String str=null;
                         try {
                             if (rs.next()) {
                                 str=rs.getString("id");
                             }
                         } catch (SQLException e) {
                             
                             e.printStackTrace();
                         }
                         return str;
                     }
                 });
             return id;
         }catch(Exception e){
             e.printStackTrace();
             return null;
         }  

     }
    
    
    /**
     * 功能:取得特定订单号 
     * 输入：sequenceName 数据库表对应的sequence的名字 
     * 输出：订单号 
     * 说明：订单号生成规则:时间(如20060412152403)+四位流水号
     * 		时间以数据库时间为准，如果以应用服务器时间为准，如果服务器时间不一致，可造成订单号重复
     * 		现在的实现很弱智，需要改进
     */		
    public final String getNextId(String sequenceName) {
       StringBuffer buf = new StringBuffer();
        buf.append("select to_char(sysdate,'yyyymmddhh24MISS')||");
        buf.append(sequenceName);
        buf.append(".nextval as id from dual");
        try
        {
            String id = (String) this.getJdbcTemplate().query(buf.toString(),

                new ResultSetExtractor() {
                    public Object extractData(ResultSet rs) {
                        String str=null;
                        try {
                            if (rs.next()) {
                                str=rs.getString("id");
                            }
                        } catch (SQLException e) {
                            // TODO 自动生成 catch 块
                            e.printStackTrace();
                        }
                        return str;
                    }
                });
            return id;
        }catch(Exception e){
            e.printStackTrace();
            return null;
        }  

    }
 
    
    /**
     * 查询记录总数
     * @param sql 查询语句，如：(select count(*) from TableName where .....)
     * @return
     */
    public final int queryCount(String sql)
    {
    	return getJdbcTemplate().queryForInt(sql);
    }
    
    /**查询记录总数 
     * @param sql
     * @param params
     * @param types
     * @return
     */
    public final int queryCount(String sql,Object[] params,int[] types){
        Integer i_count =(Integer) this.getJdbcTemplate().query(sql,params,types,new ResultSetExtractor(){
            public Object extractData(ResultSet rs) {
                int count=-1;
                try {
                    if (rs.next()) {
                        count=rs.getInt(1);	//只有一个列
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                return new Integer(count);
            }
        }
        );
        return i_count.intValue();
    }
    
    /**
     * 功能:判断sql语句是否执行成功(判断insert delete update语句的返回结果)
     *2007-4-4
     * @param batchUpdate返回的结果或update返回的结果
     * @return 执行成功返回true
     */
    public  final boolean isUpdateSuccess(int[] res){
        if (res == null || res.length == 0 ) {
            	return false;
        }
        //判断SQL影响的行数
        for (int k=0;k<res.length;k++){
            if (res[k]==0 || res[k]==Statement.EXECUTE_FAILED ){
                return false;
            }
        }
        return true;
    }
    
    /**
     * 查询字符串
     * @param sql
     * @return
     */
    public final String queryString(String sql)
    {
    	return (String)getJdbcTemplate().queryForObject(sql,String.class);
    }
    
}