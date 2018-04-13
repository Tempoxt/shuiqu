package cn.test;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

import org.json.JSONObject;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.request.AlipayOpenAuthTokenAppRequest;
import com.alipay.api.request.AlipaySystemOauthTokenRequest;
import com.alipay.api.response.AlipayOpenAuthTokenAppResponse;
import com.alipay.api.response.AlipaySystemOauthTokenResponse;
import com.fun.DemoApplication;
import com.fun.WxHB.WxPayHB;
import com.fun.bean.PayConfig;
import com.fun.bean.VendingMachineConfig;
import com.fun.bean.WXHB;
import com.fun.bean.WxPayRefundModel;
import com.fun.bean.XCXPayModel;
import com.fun.bean.vendor.VendorContainer;
import com.fun.bean.vendor.VendorDoor;
import com.fun.bean.vendor.VendorMainboard;
import com.fun.bean.vendor.VendorOrder;
import com.fun.bean.vendor.VendorProduct;
import com.fun.bean.vendor.VendorProductModel;
import com.fun.dao.IVendorContainerDao;
import com.fun.dao.IVendorDoorDao;
import com.fun.dao.IVendorMainboardDao;
import com.fun.dao.IVendorOrderDao;
import com.fun.dao.IVendorProductDao;
import com.fun.pay.AlipayPayClose;
import com.fun.pay.PayResponseReturnModel;
import com.fun.pay.WxPayRefund;
import com.fun.pay.h5.WxH5Pay;
import com.fun.pay.h5.ZFBPay;
import com.fun.service.IVendorContainerService;
import com.fun.service.IVendorOrderService;
import com.fun.util.HttpClientUtil;
import com.fun.util.OrderSave;
import com.fun.util.QiniuFileSystemUtil;
import com.fun.util.RemoteInterfaceAddress;
import com.qiniu.common.QiniuException;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = DemoApplication.class)
@WebAppConfiguration
public class DemoApplicationTest {

	@Autowired
	WxPayHB wxph;
	
	@Autowired
	WxH5Pay wxH5Pay;
	
	@Autowired
	private RemoteInterfaceAddress remoteInterfaceAddress;
	
	@Autowired
	IVendorContainerDao container;
	
	@Autowired
	IVendorDoorDao doorDao;
	
	@Autowired
	IVendorProductDao poductDao;
	
	@Autowired
	IVendorContainerService containerService;
	
	@Autowired
	IVendorMainboardDao mainboardDao;
	
	@Autowired
	IVendorOrderDao orderDao;
	
	@Autowired
	IVendorOrderService orderService;
	
	
	@Test
	public void test03() throws QiniuException{
		
		String filePath = "C:\\Users\\XHX\\Desktop\\asdfasfasdfasdfas.png";
		byte[] buffer = null;  
        try {  
            File file = new File(filePath);  
            FileInputStream fis = new FileInputStream(file);  
            ByteArrayOutputStream bos = new ByteArrayOutputStream(1000);  
            byte[] b = new byte[1000];  
            int n;  
            while ((n = fis.read(b)) != -1) {  
                bos.write(b, 0, n);  
            }  
            fis.close();  
            bos.close();  
            buffer = bos.toByteArray();  
        } catch (FileNotFoundException e) {  
            e.printStackTrace();  
        } catch (IOException e) {  
            e.printStackTrace();  
        }
		String uploadShearPic = QiniuFileSystemUtil.uploadShearPic(buffer);
		System.out.println(uploadShearPic);
	}
	
	@Test
	public void test04(){
		Integer.parseInt("sdfg");
	}
	
	@Test
	public void test05(){
		WXHB w = new WXHB();
		UUID uuid=UUID.randomUUID();
	    String str = uuid.toString(); 
	    String uuidStr=str.replace("-", "");
		w.setNonce_str(uuidStr);
		Random ran=new Random();
		int a=ran.nextInt(99999999);
		int b=ran.nextInt(99999999);
		long l=a*10000000L+b;
		String num=String.valueOf(l);
		w.setMchBillno(num);
		w.setMchId("1427007502");
		w.setWxappid(PayConfig.getWeixin_appid());
		System.out.println(PayConfig.getWeixin_appid());
		w.setSendName("谢海鑫"); // 商户名称
		w.setReOpenid("o0xutwDJu8wwa-MQCC_GXxsCAy4k"); // 用户openid
		w.setTotalAmount("300"); // 付款金额
		w.setTotalNum("3"); // 红包发放总人数
		w.setAmt_type("ALL_RAND"); // 红包金额设置方式 
		w.setWishing("测试"); // 红包祝福语
		w.setClientIp("183.238.231.83"); // Ip地址
		w.setActName("测试"); // 活动名称
		w.setRemark("备注不能空着"); // 备注
		w.setAppKey(PayConfig.getWeixin_appKey());
		String pay = wxph.Pay(w);
		System.out.println(pay);
	}
	
	@Test
	public void test06(){
		XCXPayModel xcxPayModel = new XCXPayModel();
		xcxPayModel.setAppId("wx5e24d332ca079ac1");
		xcxPayModel.setMchId("1301591101");
		UUID uuid=UUID.randomUUID();
	    String str = uuid.toString(); 
	    String uuidStr=str.replace("-", "");
		xcxPayModel.setNonceStr(uuidStr);
		xcxPayModel.setBody("售货机");
		xcxPayModel.setOrderSN(OrderSave.getOrderNew());
		xcxPayModel.setTotalFee("1");
		xcxPayModel.setCreateIp("183.238.231.83");
		xcxPayModel.setNotifyUrl("http://box.shuiqoo.cn:7080/Container_Market/evian/paynotify/weixinNotify.action");
		xcxPayModel.setScene_info("{\"h5_info\": {\"type\":\"Wap\",\"wap_url\": \"https://x10.shuiqoo.cn\",\"wap_name\": \"售货机\"}} ");
		xcxPayModel.setAppKey("ShQooappcomjkjehbnvzioejbazkmbao");
		PayResponseReturnModel pay = wxH5Pay.Pay(xcxPayModel);
		System.out.println(pay);
	}
	
	@Test
	public void test07(){
		// {"access_token":"6_8uGB1rMT-i_zi6E_5ChEN6rrNmahXAyYgSp-3NsHJ5XXw7-ase5bCKJNvKse_K872NmVj-aaR9MoIy12p49ydVa00W6doNy9WdVnCEcE59ojkC53DOpun4oKyhAt4GLibVTT3s369EKnA966KQRdAJAYEF","expires_in":7200}
		// {"errcode":40125,"errmsg":"invalid appsecret, view more at http:\/\/t.cn\/RAEkdVq hint: [KjmSya0537sha7]"}
		String url = "https://api.weixin.qq.com/cgi-bin/token";
		Map<String, String> param = new HashMap<String, String>();
		param.put("grant_type", "client_credential");
		param.put("appid", "wx5288d5e0e47b4f76");
		param.put("secret", "f2601ba78aca4888877b81f07540b892");
		String doGet = HttpClientUtil.doGet(url, param);
		System.out.println(doGet);
	}

	@Test
	public void test08(){
		System.out.println(VendingMachineConfig.getWxPayAppId());
		System.out.println(VendingMachineConfig.getWxPayAppKey());
		System.out.println(VendingMachineConfig.getWxPayMchId());
		System.out.println(VendingMachineConfig.getWxPaySecret());
		System.out.println(VendingMachineConfig.getAilPrivateKey());
		System.out.println(VendingMachineConfig.getAilPublicKey());
	}
	
	@Test
	public void test09(){
		/*List<VendorContainer> selectVendorContainerIdByMainboard = containerService.findContainerIdByMainboard(1);
		System.out.println(selectVendorContainerIdByMainboard);
		
		List<VendorDoor> selectVendorDoorByContainer = doorDao.selectVendorDoorByContainer(selectVendorContainerIdByMainboard.get(0).getBmId(),selectVendorContainerIdByMainboard.get(0).getVmId());
		System.out.println(selectVendorDoorByContainer);
		
		VendorProduct selectVendorProductById = poductDao.selectVendorProductById(selectVendorDoorByContainer.get(0).getProductId());
		System.out.println(selectVendorProductById);*/
		/*Map<String, Object> findContainersInfo = containerService.findContainersInfo(1);
		System.out.println(findContainersInfo);
		*
		*/
		/*List<VendorMainboard> selectMainboardBymainboardNoMD5 = mainboardDao.selectMainboardBymainboardNoMD5("97a0bb0d2a67eff");
		System.out.println(selectMainboardBymainboardNoMD5.size());*/
		VendorDoor selectDoorByContainerOrDoor = doorDao.selectDoorByContainerOrDoor(1, 1, 1);
		System.out.println(selectDoorByContainerOrDoor);
	}
	
	@Test
	public void test10(){
		Map<String, Object> addOrderMap = orderDao.insertOrder("",2,"97a0b7b0d2a67eff", 1, 1, 0.01, 0.01, "");
		System.out.println(addOrderMap);
	}
	
	@Test
	public void test11(){
		String alipay_auth_code="a17db519d9484f2d85d6205be7c7PX36";
		AlipayClient alipayClient = new DefaultAlipayClient("https://openapi.alipay.com/gateway.do",VendingMachineConfig.getAilPayAppId(),VendingMachineConfig.getAilPrivateKey() ,"json","utf-8",VendingMachineConfig.getAilPublicKey() ,"RSA2");
		AlipaySystemOauthTokenRequest requestLogin1 = new AlipaySystemOauthTokenRequest();
		requestLogin1.setCode(alipay_auth_code);
		requestLogin1.setGrantType("authorization_code");
		try {
			AlipaySystemOauthTokenResponse responseToken = alipayClient.execute(requestLogin1);
			String userId=null;
			String access_token=null;
			System.out.println("sub_msg : "+responseToken.getSubMsg());
			if(responseToken.isSuccess()){
			    
			    access_token=responseToken.getAlipayUserId();
			    String alipayUserId=responseToken.getAlipayUserId();
			    userId=responseToken.getUserId();
			    System.out.println("access_token : "+access_token);
			    System.out.println("alipayUserId : "+alipayUserId);
			    System.out.println("userId : "+userId);
			    System.out.println(responseToken.getMsg());
			    System.out.println(responseToken.getAuthTokenType());
			    System.out.println(responseToken.getBody());
			    System.out.println(responseToken.getCode());
			    System.out.println(responseToken.getExpiresIn());
			    System.out.println(responseToken.getMsg());
			    System.out.println(responseToken.getReExpiresIn());
			    System.out.println(responseToken.getRefreshToken());
			    System.out.println(responseToken.getSubCode());
			    
			} else {
				System.out.println("没有获取到支付宝userId : " +responseToken.getMsg());
			}
		} catch (AlipayApiException e) {
			// TODO Auto-generated catch block
			System.out.println("AlipayApiException : "+e.getErrMsg());
		}
	}
	

	@Test
	public void test12(){
		AlipayPayClose payClose = new AlipayPayClose("1803091208051600000109875");
		payClose.payClose();
	}
	
	@Test
	public void test13(){
		List<VendorMainboard> selectMainboardBymainboardNoMD5 = mainboardDao.selectMainboardBymainboardNoMD5("97a0b7b0d2a67eff");
		/*List<Object> selectDoorByContainer = doorDao.selectDoorProductByContainer2(1, null);*/
		System.out.println(selectMainboardBymainboardNoMD5);
	}
	
	@Test
	public void test14(){
		VendorOrder selectOrderByOrderId = orderDao.selectOrderByOrderId(39);
		BigDecimal bigDecimal = new BigDecimal(selectOrderByOrderId.getRealityPrice().toString());
		BigDecimal big100 = new BigDecimal("100");
		System.out.println((big100.multiply(bigDecimal).intValue())+"");
	}
	
	@Test
	public void test15(){
		WxPayRefundModel wx = new WxPayRefundModel();
		wx.setAppId("wx57c89676f397cfd1");
		wx.setAppKey("Qysoftapph2oLIFE8585668339634001");
		wx.setMchId("1316186301");
		UUID uuid=UUID.randomUUID();
	    String str = uuid.toString(); 
	    String uuidStr=str.replace("-", "");
		wx.setNonceStr(uuidStr);
		String orderNew = OrderSave.getOrderNew();
		System.out.println("setOutRefundNo ="+orderNew);
		wx.setOutRefundNo(orderNew);
		wx.setOutTradeNo("180312180229540000010959");
		wx.setRefundFee("2");
		wx.setTotalFee("2");
		WxPayRefund a = new WxPayRefund();
		String pay = a.Pay(wx);
		System.out.println(pay);
	}

	@Test
	public void test16(){
		ZFBPay pay = new ZFBPay();
		JSONObject refund = pay.refund(0.01, "1803131508028270000108607");
		System.out.println(refund);
	}
	
	@Test
	public void test17(){
		List<VendorOrder> findOrderByUserId = orderService.findOrderByUserId("oGLfUwE5ZHCB_d5tWdXaxZ-MOq_k", 1, 10);
		System.out.println(findOrderByUserId);
		for (VendorOrder vendorOrder : findOrderByUserId) {
			System.out.println(vendorOrder);
			
		}
	}
	
	@Test
	public void test18(){
		List<VendorProductModel> selectMcIdByBmId = doorDao.selectMcIdByBmId(1);
		System.out.println(selectMcIdByBmId);
		List<VendorMainboard> selectMainboardBymainboardNoMD5 = mainboardDao.selectMainboardBymainboardNoMD5("97a0b7b0d2a67eff");
		for (VendorMainboard vendorMainboard : selectMainboardBymainboardNoMD5) {
			System.out.println(vendorMainboard);
		}
		/*List<Map<String, Object>> selectTest = orderDao.selectTest("oGLfUwE5ZHCB_d5tWdXaxZ-MOq_k", null, null);
		for (Map<String, Object> map : selectTest) {
			System.out.println(map);
		}*/
	}
}
