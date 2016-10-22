package com.ai.job.scan;

import java.io.InputStream;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.ai.job.scan.dao.BrowserScanDao;
import com.ai.job.scan.exception.BusinessException;
import com.ai.job.scan.service.BrowserScanService;

public class TestJunit {

	@Before
	public void setUp() throws Exception {
		
	}

	@Test
	public void test() { //mybatis的配置文件
//        String resource = "mybatis/Configuration.xml";
//        //使用类加载器加载mybatis的配置文件（它也加载关联的映射文件）
//        InputStream is = TestJunit.class.getClassLoader().getResourceAsStream(resource);
//        //构建sqlSession的工厂
//        SqlSessionFactory sessionFactory = new SqlSessionFactoryBuilder().build(is);
//        //使用MyBatis提供的Resources类加载mybatis的配置文件（它也加载关联的映射文件）
//        //Reader reader = Resources.getResourceAsReader(resource); 
//        //构建sqlSession的工厂
//        //SqlSessionFactory sessionFactory = new SqlSessionFactoryBuilder().build(reader);
//        //创建能执行映射文件中sql的sqlSession
//        SqlSession session = sessionFactory.openSession();
//        List<Map<String,String>> urlList = session.selectList("JobScanDao.queryUrlInfos", null);
//        System.out.println(urlList);
//        BrowserScanDao dao = new BrowserScanDao();
//        dao.setSession(session);
//        BrowserScanService serv = new BrowserScanService();
//        serv.setScanDao(dao);
//        try {
//			serv.scanUrl();
//		} catch (BusinessException e) {
//			e.printStackTrace();
//		}
//        
        
        
        
        BeanFactory factory = 
        		new ClassPathXmlApplicationContext(
        				new String[]{"spring/service.xml","spring/factory.xml"});  
        BrowserScanService browserScanService = 
        		(BrowserScanService) factory.getBean("browserScanService");
        try {
			browserScanService.scanUrl();
		} catch (BusinessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
