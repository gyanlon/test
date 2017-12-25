<jsp:directive.include file="includes/top.jsp" />
  <div id="msg" class="errors">
   <script type="text/javascript">  
            <%Boolean isFrame = (Boolean) request.getAttribute("isFrame");
			Boolean isLogin = (Boolean) request.getAttribute("isLogin");
			// 
			if (isLogin) {
				if (isFrame) {%>  
                        parent.location.replace('${service}?ticket=${ticket}');  
                    <%} else {%>  
                        location.replace('${service}?ticket=${ticket}'); 
                    <%}
			}%>  
            // 
            ${callback}({'login':${isLogin ? '"success"': '"fails"'}, 'msg': ${isLogin ? '""': '"error"'}}); 
        </script>
  </div>
<jsp:directive.include file="includes/bottom.jsp" />