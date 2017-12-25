<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%
String path = request.getContextPath();  
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/"; 
%>
<link rel="stylesheet" type="text/css" href="css/login.css" />
<object classid="clsid:707C7D52-85A8-4584-8954-573EFCE77488"
	id="JITDSignOcx" width="0"
	codebase="<%=basePath %>JITDSign.cab#version=2,0,24,19"></object>
<script type="text/javascript"
	src="<%=basePath %>js/jquery/jquery-1.11.3.min.js"></script>
<script type="text/javascript">
    var threeUrl="http://10.44.20.43:9080/cas/login";
    var casUrl=location.protocol+"//"+location.host+"${pageContext.request.contextPath}";
	var service = '${service}';
	var start = service.indexOf("//");
	var http = service.substring(0, start + 2);
	var tempService = service.substring(start + 2);
	start = tempService.indexOf("/");
	var end = tempService.indexOf("/", start + 1);
	var domain = http + tempService.substring(0, end);
	$(document).ready(function() {
		$("#targetService").val(service);
		flushLoginTicket();
	});

	//登录验证函数
	var loginValidate = function() {
		var msg;
		if ($.trim($("#username").val()).length == 0) {
			msg = "用户名不能为空";
		} else if ($.trim($("#password").val()).length == 0) {
			msg = "密码不能为空";
		}
		if (msg && msg.length > 0) {
			alert(msg);
			return false;
		} else {
			if(caAuth()){//不管用户类型如何,都需操作CA,CA成功,检查用户类型
				checkUserType();
			}
		}
	};

	var checkUserType = function() {
		var url = domain + "/checkUserType";
		var formParam = $("#loginform").serialize();//序列化表单内容为字符串  
		$.ajax({
			url : url,
			type : 'post',
			data : formParam,
			async : true,
			dataType : "jsonp",
			jsonp : "callbackparam",//服务端用于接收callback调用的function名的参数  
			jsonpCallback : "success_jsonpCallback",//callback的function名称  
			error : function(XMLHttpRequest, textStatus, errorThrown) {
				//alert(XMLHttpRequest.status);
				//alert(XMLHttpRequest.readyState);
				//alert(textStatus);
				alert('检查用户错误');
			},
			success : function(data) {
			}
		});
	};

	function success_jsonpCallback(result) {
		if (result.isSuccess == true) {
			if (result.isThree == true) {
				alert('用户为公共用户');
				loginByOther();
			} else {
				alert('用户为专有用户');
				$("#loginform").submit();
			}
		} else {
			alert(result.msg);
		}
	}

	//////////////
	//采用其他方式进行验证登录
	var loginByOther = function() {
		var url = threeUrl+'?service='
				+ encodeURI(service + "?w=other_cas_auth");
		$("#loginform").attr('action', url);
		$("#loginform").submit();
	};

	// 由于一个 login ticket 只允许使用一次, 当每次登录需要调用该函数刷新 lt 
	var flushLoginTicket = function() {
		var _services = 'service=' + encodeURIComponent(service);
		$.getScript(threeUrl+'?' + _services
				+ '&get-lt=true&n=' + new Date().getTime(), function() {
			// 将返回的 _loginTicket 变量设置到 input name="lt" 的value中。 
			$('#J_LoginTicket').val(_loginTicket);
		});
		// Response Example: 
		// var _loginTicket = 'e1s1'; 
	};
	////////////
	
	function caAuth(){
		var isOk=true;
		//return true;
		doDataProcess();
		var url = casUrl+"/caauth";
		var formParam = $("#loginform").serialize();//序列化表单内容为字符串  
		$.ajax({
			url : url,
			type : 'post',
			data : formParam,
			async : false,
			error : function(XMLHttpRequest, textStatus, errorThrown) {
				//alert(XMLHttpRequest.status); 
				//alert(XMLHttpRequest.readyState); 
				//alert(textStatus); 
				alert('CA认证错误');
			},
			success : function(data) {
			    var result=eval('(' + data + ')');
				if(result.isSuccess){
					alert('CA认证成功');
					//$("#loginform").submit();
				}else{
					isOk=false;
					alert('CA认证失败：'+result.msg);
				}
			}
		});
		return isOk;
	}
	//根据原文和证书产生认证数据包
function doDataProcess(){
	var Auth_Content = $("#username").val();
	var DSign_Subject = document.getElementById("RootCADN").value;
	if(Auth_Content==""){
		alert("认证原文不能为空!");
	}else{
		//控制证书为一个时，不弹出证书选择框
		JITDSignOcx.SetCertChooseType(1);
		JITDSignOcx.SetCert("SC","","","",DSign_Subject,"");
		if(JITDSignOcx.GetErrorCode()!=0){
			alert("错误码："+JITDSignOcx.GetErrorCode()+"　错误信息："+JITDSignOcx.GetErrorMessage(JITDSignOcx.GetErrorCode()));
			return false;
		}else {
			 var temp_DSign_Result = JITDSignOcx.DetachSignStr("",Auth_Content);
			 if(JITDSignOcx.GetErrorCode()!=0){
					alert("错误码："+JITDSignOcx.GetErrorCode()+"　错误信息："+JITDSignOcx.GetErrorMessage(JITDSignOcx.GetErrorCode()));
					return false;
			 }
		//如果Get请求，需要放开下面注释部分
		//	 while(temp_DSign_Result.indexOf('+')!=-1) {
		//		 temp_DSign_Result=temp_DSign_Result.replace("+","%2B");
		//	 }
			document.getElementById("signed_data").value = temp_DSign_Result;
		}
	}
	document.getElementById("auth_data").value = Auth_Content;
	//document.forms[0].submit();
}
	
function reset(){
	$("#loginform").reset();
}

 function clearMe(obj){
		obj.value ="";
	 }
</script>
</head>
<body>
	<div class="main">
	    <div class="top"></div>
		<div class="box">
			<form id="loginform" action="noflow" method="post">
				<div class="inputBox"><input id="username" name="username" type="text" onclick="clearMe(this)" value="用户名" ></input></div>
				<div class="inputBox" style="margin-top:0px"><input id="password" type="password"  name="password" onclick="clearMe(this)" value="请输入密码"></input></div>
				<div class="buttonImg">
					<img src="images/dl_1.png" onclick='loginValidate();'></img><img src="images/cz_1.png" onclick="reset()"></img>
				<input type="button" onclick="loginValidate1()"
					value="其他登陆" />
					<input type="button" name="b_refer1" onclick="caAuth();" value="认证" />
				</div>
			<input type="hidden" name="lt" value="" id="J_LoginTicket" />
			<input type="hidden" name="_eventId" id="_eventId" value="submit" />
			<input type="hidden" id="signed_data" name="signed_data" />
			<input type="hidden" id="auth_data" name="auth_data" />
			<input type="hidden" id="RootCADN" value="" width="30" />
			<input type="hidden" id="targetService" name="service" value="" />
			</form>
		</div>
	</div>
</body>