<%--

    Licensed to Jasig under one or more contributor license
    agreements. See the NOTICE file distributed with this work
    for additional information regarding copyright ownership.
    Jasig licenses this file to you under the Apache License,
    Version 2.0 (the "License"); you may not use this file
    except in compliance with the License.  You may obtain a
    copy of the License at the following location:

      http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing,
    software distributed under the License is distributed on an
    "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
    KIND, either express or implied.  See the License for the
    specific language governing permissions and limitations
    under the License.

--%>
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
