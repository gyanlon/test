<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>
 <link rel="stylesheet" href="css/main/main3/login.css" type="text/css"/>
<object classid="clsid:707C7D52-85A8-4584-8954-573EFCE77488"
	id="JITDSignOcx" width="0" height="50px"
	codebase="<%=basePath%>JITDSign.cab#version=2,0,24,19"></object>
<script type="text/javascript"
	src="<%=basePath%>js/jquery/jquery-1.11.3.min.js"></script>
	<script type="text/javascript" src="<%=basePath%>js/layer/layer/layer.js"></script>
<script type="text/javascript">
	document.onkeydown = function(e){//添加回车事件
	    if(!e) e = window.event;//火狐中是 window.event
	    if((e.keyCode || e.which) == 13){
	    	loginValidate();
	    }
	};
	function checkIe() {
        if (navigator.appName == "Microsoft Internet Explorer" && navigator.appVersion.split(";")[1].replace(/[ ]/g, "") == "MSIE6.0") {
            return true;
        }
        else if (navigator.appName == "Microsoft Internet Explorer" && navigator.appVersion.split(";")[1].replace(/[ ]/g, "") == "MSIE7.0") {
            return true;
        }
        else if (navigator.appName == "Microsoft Internet Explorer" && navigator.appVersion.split(";")[1].replace(/[ ]/g, "") == "MSIE8.0") {
            return true;
        }
        else if (navigator.appName == "Microsoft Internet Explorer" && navigator.appVersion.split(";")[1].replace(/[ ]/g, "") == "MSIE9.0") {
            return true;
        }
        return false;
    }
    
    function checkIeFailure(){
        layer.confirm('当前浏览器版本太低，是否升级浏览器获得更好的体验？', {
            btn: ['好的'] //按钮
        }, function(){
        	downBrowserWin();
        });
    }
    
	var service = '${service}';
	$(document).ready(function() {
		if(!checkIe()){
	        $('#tips').text('');
	    }else{
	        checkIeFailure();
	    }
		$("#targetService").val(service);
	});

	function downBrowser(name) {
        window.location.href = "http://10.100.9.66:8081/baseplatform/uploadController/downLoadBrowser/" + name + '.do';
        layer.closeAll();
    }
	
	function downBrowserWin(){
        layer.open({
            type: 1,
            shade: 0.5,
            move:false,
            area: ['420px', '240px'], //宽高
            content: '<div> <table class="browser" style="width: 360px; height: 175px; margin-left: 20px;"> <tr> <th width="70%"   > 浏览器名称</th><th>操作</th></tr> <tr> <td >360se8.1.exe<span class="recommend">（推荐）</span></td> <td ><a onclick="downBrowser(\'360se8.1.exe\')"  href="#" title="下载">下载</a> </td> </tr><tr> <td >Chrome47.exe</td> <td ><a onclick="downBrowser(\'Chrome47.exe\')"  href="#" title="下载">下载</a> </td> </tr> <tr> <td >FireFox41.exe</td> <td><a onclick="downBrowser(\'FireFox41.exe\')" href="#"  title="下载">下载</a> </td> </tr>  <tr> <td>自行搜索下载</td> <td><a target="_blank" href="https://www.baidu.com/" title="跳转">跳转</a> </td> </tr></table> </div>',
            cancel: function(index){
                layer.closeAll();
            }
        });
    }
	 
    
    
    
	//登录验证函数
	var loginValidate = function() {
		 if (checkIe()) {
             checkIeFailure();
         }else {
			var msg;
			if ($.trim($("#username").val()).length == 0) {
				msg = "用户名不能为空";
			} else if ($.trim($("#password").val()).length == 0) {
				msg = "密码不能为空";
			}
			if (msg && msg.length > 0) {
				layer.msg(msg, {icon: 4, time: 3000});;
				return false;
			} else {
				loginByAjax();
			}
         }
	};

	function loginByAjax() {
		//doDataProcess();//操作CA证书
		var url = "<%=basePath%>noflow1";
		var datas = $("#loginform").serialize();
		$.ajax({
			type : 'post',
			dataType : 'json',
			url : url,
			data : datas,
			success : function(result) {
				if (result.result.code == '200') {
					location.href = result.result.msg;
				} else {
					layer.msg(result.result.msg, {icon: 4, time: 3000});
				}
			}
		});
	}

	/**
	CA登录
	*/
	function loginCAValidate(){
		if(doDataProcess()){
			var url = "<%=basePath%>noflow0";
			$.ajax({
				type : 'post',
				dataType : 'json',
				url : url,
				data : {'auth_data':$("#auth_data").val(),'signed_data':$("#signed_data").val(),"service":$("#targetService").val()},
				success : function(result) {
					if (result.result.code == '200') {
						location.href = result.result.msg;
					} else {
						layer.msg(result.result.msg, {icon: 4, time: 3000});
					}
				}
			});
		}
	}
	
	function getOriginal(){
		var url = "<%=basePath%>randomAction";
		$.ajax({
			type : 'post',
			dataType : 'json',
			url : url,
			success : function(result) {
				if (result.result.code == '200') {
					$("#auth_data").val(result.result.msg);
				} else {
					alert(result.result.msg);
				}
			}
		});
	}
	
	//根据原文和证书产生认证数据包
	function doDataProcess() {
		var Auth_Content = $("#auth_data").val();
		var DSign_Subject = document.getElementById("RootCADN").value;
		if (Auth_Content == "") {
			alert("认证原文不能为空!");
			return false;
		} else {
			//控制证书为一个时，不弹出证书选择框
			JITDSignOcx.SetCertChooseType(1);
			JITDSignOcx.SetCert("SC", "", "", "", DSign_Subject, "");
			if (JITDSignOcx.GetErrorCode() != 0) {
				alert("错误码：" + JITDSignOcx.GetErrorCode() + "　错误信息：" + JITDSignOcx.GetErrorMessage(JITDSignOcx.GetErrorCode()));
				return false;
			} else {
				var temp_DSign_Result = JITDSignOcx.DetachSignStr("", Auth_Content);
				if (JITDSignOcx.GetErrorCode() != 0) {
					alert("错误码：" + JITDSignOcx.GetErrorCode() + "　错误信息：" + JITDSignOcx.GetErrorMessage(JITDSignOcx.GetErrorCode()));
					return false;
				}
				//如果Get请求，需要放开下面注释部分
				//	 while(temp_DSign_Result.indexOf('+')!=-1) {
				//		 temp_DSign_Result=temp_DSign_Result.replace("+","%2B");
				//	 }
				document.getElementById("signed_data").value = temp_DSign_Result;
			}
		}
		//document.getElementById("auth_data").value = Auth_Content;
		return true;
		//document.forms[0].submit();
	}

	/* function reset() {
		$("#loginform").reset();
	} 

	function clearMe(obj) {
		obj.value = "";
	}*/
	
</script>


<style>
    .layui-layer-dialog{
        top:50%!important;
        margin-top: -120px!important;
        left: 50%!important;
        margin-left: -210px!important;
    }
    .layui-layer-msg{
        left: 50%!important;
        margin-left: -150px!important;
    }
</style>
<script type="text/javascript">
	/* function setTab(name, cursel) {
		cursel_0 = cursel;
		for ( var i = 1; i <= links_len; i++) {
			var menu = document.getElementById(name + i);
			var menudiv = document.getElementById("con_" + name + "_" + i);
			if (i == cursel) {
				menu.className = "off";
				menudiv.style.display = "block";
			} else {
				menu.className = "";
				menudiv.style.display = "none";
			}
			if(cursel==2){
				getOriginal();
			}
		}
	}
	onload = function() {
		var links = document.getElementById("tab1").getElementsByTagName('li');
		links_len = links.length;
	}; */
	
</script>

</head>
<body>
    <!--整个页面-->
    <div class="main">
        <div class="content">
            <form class="loginform" id="loginform" method="get" action="noflow0">
            <input type="hidden" id="targetService" name="service" value="" />
                <div class="row" style="padding-left: 70px;">
                    <label for="username">用户名</label>
                    <input style="margin-left: 15px" type="text" id="username" name="username">
                </div>
                <div class="row" style="margin-top: 15px;padding-left: 80px;">
                    <!--a href="#" class="pull-right label-forgot">Forgot password?</a-->
                    <label for="password">密码</label>
                    <input style="margin-left: 22px" type="password" id="password" name="password">
                </div>
                <div class="row" style="margin-top: 15px;padding-left: 140px;">
                    <button class="btn" type="button" id="subBtn" onclick="loginValidate();">
                        		登 录
                    </button>
                </div>
            </form>

        </div>

    <div class="bottom">
        <span id="tips">请使用IE11或360极速浏览器（点击地址栏后面闪电标记，切换极速模式） <a  href="downBrowserWin()"  title="下载" class="download">点击下载</a></span>
    </div>
    </div>

    <div class="loginbg"></div>



</body>



<!-- <body>
	<div class="main">
		<div class="top"></div>
		<div class="box">
			<div class="tab1" id="tab1">
				<div class="menu">
					<ul>
						<li id="one1" onclick="setTab('one',1)" class="off">用户登录</li>
						<li id="one2" onclick="setTab('one',2)">CA认证</li>
					</ul>
				</div>
				<div class="menudiv">

					<div id="con_one_1">
						<form id="loginform" action="noflow0" method="post"
							style="background: url('images/tp.jpg') no-repeat 90px 30px;padding-left:20px;">
							<div class="inputBox">
								用户名：<input id="username" name="username" type="text"></input>
							</div>
							<div class="inputBox" style="margin-top: 30px;">
								密　码：<input id="password" type="password" name="password"></input>
							</div>
							<div class="buttonImg">
								<img src="images/dl_1.png" onclick='loginValidate();'></img><img
									src="images/cz_1.png" onclick="reset()"></img>
							</div>
							<input
								type="hidden" id="targetService" name="service" value="" />
						</form>
					</div>
					<div id="con_one_2" style="display: none;">

						<div class="buttonImg" style="margin-top:80px;margin-left:80px;">
							<img src="images/dl_1.png" onclick='loginCAValidate();'></img>
							<input type="hidden" id="signed_data" name="signed_data" /> <input
								type="hidden" id="auth_data" name="auth_data" /> <input
								type="hidden" id="RootCADN" value="" width="30" /> 
						</div>

					</div>
				</div>
			</div>

		</div>
	</div>
</body> -->
</html>