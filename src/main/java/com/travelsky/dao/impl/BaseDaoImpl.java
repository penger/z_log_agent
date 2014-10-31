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
     * ���ܣ��ж����ݼ����Ƿ�����ض����� 
     * ���룺���ݼ������� 
     * �����true | false 
     * ��ע��
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
     * ���ܣ��ж�HashMap���Ƿ�����ض����������ݼ��õ�ĳ�е�����ֵ 
     * ���룺���ݼ������� 
     * �����true | false 
     * ��ע��
     */
    public boolean hasColumn(Map allColumns, String clmName) {
        if (allColumns.containsKey(clmName.toUpperCase()))
            return true;
        return false;
    }

    /**
     * ����:��ҳ��ѯ 
     * ���룺 ����SQL Mapper�� ��ʼֵ(��1��ʼ) ���� 
     * �����List
     * ˵�������RowMapper�̳���BaseRowMapper���򴫵�ʱ������new XxxRowMapper,��������ͬ������
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
     * ����:��ҳ��ѯ 
     * ���룺 SQL SQL�еĲ���(��PreparedStatement����Ҫ���õĲ���) Mapper�� ��ʼֵ(��1��ʼ) ����
     * �����List 
     * ˵����spring���ݲ����ж��������ƥ�䵽SQL�ж�Ӧ����
     * ���RowMapper�̳���BaseRowMapper���򴫵�ʱ������new XxxRowMapper,��������ͬ������
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
     * ����:��ҳ��ѯ 
     * ���룺 SQL SQL�еĲ���(��PreparedStatement����Ҫ���õĲ���) �����ж��������ӳ�䵽SQL�е�����
     * Mapper�� ��ʼֵ(��1��ʼ) ���� 
     * �����List 
     * ˵�������RowMapper�̳���BaseRowMapper���򴫵�ʱ������new
     * XxxRowMapper,��������ͬ������
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
     * ����:��ѯ 
     * ���룺 SQL Mapper�� 
     * �����List 
     * ˵�������RowMapper�̳���BaseRowMapper���򴫵�ʱ������new
     * XxxRowMapper,��������ͬ������
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
     * ����:��ѯ 
     * ���룺 SQL SQL�еĲ���(��PreparedStatement����Ҫ���õĲ���) Mapper�� 
     * �����List
     * ˵�������RowMapper�̳���BaseRowMapper���򴫵�ʱ������new XxxRowMapper,��������ͬ������
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
     * ����:��ѯ 
     * ���룺 SQL SQL�еĲ���(��PreparedStatement����Ҫ���õĲ���) �����ж��������ӳ�䵽SQL�е�����
     * Mapper�� 
     * �����List 
     * ˵�������RowMapper�̳���BaseRowMapper���򴫵�ʱ������new
     * XxxRowMapper,��������ͬ������
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
     * ����:���ӡ�ɾ�����޸� 
     * ���룺 SQL SQL�еĲ���(��PreparedStatement����Ҫ���õĲ���)
     * �����ж��������ӳ�䵽SQL�е����� 
     * �����int ˵����
     */
    public int update(String sql, Object[] params, int[] types) {
    	if(ManagerFactory.getConfigController().getPropertyAsInteger("ALL", Config.IS_PRINT_SQL)==1){
			log.info(sql	);
		}
        return getJdbcTemplate().update(sql, params, types);
    }

    /**
     * ����:�������� 
     * ���룺 SQL BatchPreparedStatementSetter 
     * �����int[]
     * ˵�����ص�setter��setValues����
     */
    public int[] batchUpdate(String sql, BatchPreparedStatementSetter setter) {
        return getJdbcTemplate().batchUpdate(sql, setter);
    }

    /**
     * ����:ִ��SQL 
     * ���룺 SQL 
     * �����void 
     * ˵����
     */
    public void execute(String sql) {
        try {
        	//���˵�һ��map��û�е�ʱ������ѭ�������!
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
     * �����һ��seq
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
     * ����:ȡ���ض������� 
     * ���룺sequenceName ���ݿ���Ӧ��sequence������ 
     * ����������� 
     * ˵�������������ɹ���:ʱ��(��20060412152403)+��λ��ˮ��
     * 		ʱ�������ݿ�ʱ��Ϊ׼�������Ӧ�÷�����ʱ��Ϊ׼�����������ʱ�䲻һ�£�����ɶ������ظ�
     * 		���ڵ�ʵ�ֺ����ǣ���Ҫ�Ľ�
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
                            // TODO �Զ����� catch ��
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
     * ��ѯ��¼����
     * @param sql ��ѯ��䣬�磺(select count(*) from TableName where .....)
     * @return
     */
    public final int queryCount(String sql)
    {
    	return getJdbcTemplate().queryForInt(sql);
    }
    
    /**��ѯ��¼���� 
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
                        count=rs.getInt(1);	//ֻ��һ����
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
     * ����:�ж�sql����Ƿ�ִ�гɹ�(�ж�insert delete update���ķ��ؽ��)
     *2007-4-4
     * @param batchUpdate���صĽ����update���صĽ��
     * @return ִ�гɹ�����true
     */
    public  final boolean isUpdateSuccess(int[] res){
        if (res == null || res.length == 0 ) {
            	return false;
        }
        //�ж�SQLӰ�������
        for (int k=0;k<res.length;k++){
            if (res[k]==0 || res[k]==Statement.EXECUTE_FAILED ){
                return false;
            }
        }
        return true;
    }
    
    /**
     * ��ѯ�ַ���
     * @param sql
     * @return
     */
    public final String queryString(String sql)
    {
    	return (String)getJdbcTemplate().queryForObject(sql,String.class);
    }
    
}