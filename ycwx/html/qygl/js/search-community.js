var d = dialog({contentType:'load', skin:'bk-popup'}).show();

var Request = GetRequest(),
	town = Request["town"];
var $communityView = $('#community_view'),
	$communityList = $('#community_list'),
	$noResultWrap = $('#no_result_wrap');

// 搜索框
$('.searchbar').searchBar();

var reqPromise = function(url,data) {
	 return new Promise(function(resolve, reject) {
		sendPost(url, data, "json", "post",
		  	function queryFailed (req) {
		  		
				dialog({contentType:'tipsbox', skin:'bk-popup' , content:'加载失败'}).show();
				// TODO 开启示例数据
				resolve({});
			}
	  		, function success(req) {
				resolve(req);
	  	});
	});
};
reqPromise("patient/hosptail/getHositalByTownCode",{town:town}).then(function(data) {
	// TODO 示例数据
	data = {"msg":"查询成功","list":[{"code":"3502050100","name":"海沧区嵩屿街道社区卫生服务中心"},{"code":"3502050101","name":"海沧社区卫生服务站"},{"code":"3502050200","name":"石塘社区卫生服务中心"},{"code":"3502050300","name":"东孚卫生院"},{"code":"3502050301","name":"天竺社区卫生服务站"},{"code":"3502050302","name":"国营厦门第一农场社区卫生服务站"},{"code":"3502050400","name":"新阳社区卫生服务中心"},{"code":"0a11148d-5b04-11e6-8344-fa163e8aee56","name":"厦门市海沧医院","photo":""}],"status":200};
	if(data && data.list.length) {
		$noResultWrap.hide();
		var html = template("community_li_tmpl", data);
		$communityList.html(html);
	} else {
		$noResultWrap.show();
	}
	
	d.close();
	
}).catch(function(e) {
	console && console.error(e)
});