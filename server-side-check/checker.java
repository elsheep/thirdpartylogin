public boolean checkWeiboToken(Users user){
		Map<String, String> params = new HashMap<String, String>();
		params.put("access_token", user.getThirdPartyToken());
		String returnValue = HttpClientUtils.doPost(
				"https://api.weibo.com/oauth2/get_token_info", params, HttpClientUtils.client);
		System.out.println(returnValue);
		return returnValue != null;
	}
	
	public boolean checkWechatToken(Users user){
		String returnValue = HttpClientUtils.doGet(
				String.format(
						"https://api.weixin.qq.com/sns/auth?access_token=%s&openid=%s", 
						user.getThirdPartyToken(),
						user.getWxId()), 
				HttpClientUtils.client);
		Map<Object, Object> result = JacksonUtil.GetMap(returnValue);
		int errCode = Integer.parseInt(result.get("errcode").toString());
		return errCode == 0;
	}
	
	public boolean checkQQToken(
			Users user){
		String combineParam = 
				"appid=" + phoenixProperty.getQqAppId() + "&"
				+ "format=" + "json" + "&"
				+ "openid=" + user.getQqId() + "&"
				+ "openkey=" + user.getThirdPartyToken() + "&"
				+ "pf=" + user.getPf() + "&"
				+ "userip=127.0.0.1";
		System.out.println(combineParam);
		String encodeParam = "";
		try{
			encodeParam = URLEncoder.encode(combineParam, "utf-8");
			encodeParam = "GET&%2Fv3%2Fuser%2Fget_info&" + encodeParam;
			String key = phoenixProperty.getQqAppKey() + "&";
			byte [] macCode = CryptalUtil.hamcsha1(encodeParam.getBytes(), key.getBytes());
			String requestUrl = 
					"http://openapi.tencentyun.com/v3/user/get_info?" 
							+ combineParam 
							+ "&sig=" 
							+ URLEncoder.encode(CryptalUtil.encodeBase64(macCode), "utf-8");
			String resultValue = HttpClientUtils.doGet(requestUrl, HttpClientUtils.client);
			Map<Object, Object> ret = JacksonUtil.GetMap(resultValue);
			log.debug(JacksonUtil.GetJSON(ret));
			if(ret != null && ret.get("ret") != null && Integer.parseInt(ret.get("ret").toString()) == 0){
				return true;
			}else{
				return false;
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return true;
	}