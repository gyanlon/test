版本：layer-v1.9.3
1、在.layui-layer-title 中添加了 text-align: left;样式，原来的标题集成了别的样式，居中了
2、在assets/plugins/win/layer/layer/layer.js文件中的open方法，
	添加了loadType=layerIframe的参数，因为打开的窗口要是超时了，就需要根据参数判断，然后返回登录页面
3、更改了config={type:0,shade:0.03，shade的默认值从.3改为0.03
