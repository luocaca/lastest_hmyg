$(function(){
	getCount();
	initEvent();
});

// 获取消息通知总数，需返回未读数量
var layIndex;
function getCount(){
	layIndex = layer.load(2, {shade: 0.1});
	$("#J_laypage").empty();
	$("#J_all-list").empty();
	var panel = $("#J_data-panel");
	clearPanel("#J_data-panel");
	
	$.getJSON(adminPath + "/notice/ajax/count?random=" + new Date().getTime(), $("#J_list-form").serialize(), function(result){
		if (result.success){
			var allCount = result.data.allCount;
			var unReadCount = result.data.unReadCount;
			$(".J_unread-count").html(unReadCount);
			$(".J_total-count").html(allCount);
			
			if (allCount > 0){
				loadLayPage(allCount);
			}
			else{
				closeLoading();
				panel.append(template("tpl-nodata", {tips : "暂无通知！"}));
			}
		}
		else{
			closeLoading();
			panel.append($("#tpl-error").html());
		}
	});
}

// 判断是否是不是第一次加载
var firstLoad = true;
function loadLayPage(total){
	// 每页出现的数量
	var page = 20;
	// 总页数
	var totalpage = Math.ceil(total/page); 
	laypage({
	    cont: "J_laypage",
	    pages: totalpage,
	    first: 1,
	    last: totalpage,
	    groups: 3,
	    jump: function(obj){
	    	// 若不是第一次加载，清空列表
	    	if (!firstLoad){
	    		layIndex = layer.load(2, {shade: 0.1});
	    		$("#J_all-list").empty();
	    	}
	    	
	    	// 每页数据渲染
	        var currpage = obj.curr;
	        getList(currpage);
	        firstLoad = false;
	    }
	})
}

// 消息通知列表,需做已读判断
function getList(page){
	$.getJSON(adminPath + "/notice/ajax/list?pageIndex=" + (page - 1) + "&pageSize=20&random=" + new Date().getTime(), $("#J_list-form").serialize(), function(result){
		closeLoading();
		if (result.success){
			$("#J_all-list").empty();
			var data = result.data.page.data;
			for (var i = 0; i < data.length; i++){
				var node = $(template("tpl-letter-list", data[i]));
				node.data("data", data[i]);
				$("#J_all-list").append(node);
			}
			$("#J_all-list").find(".nslog").nslog();
		}
		else {
			layer.msg(result.info);
		}
		
		backTop();
	});
}

// 全选
function checkAll(){
	var checkStatus = getEvtTgt().is(':checked');
	$("#J_all-list").find(":checkbox").each(function(){
		this.checked = checkStatus;
	});
}

// 标记为已读
function signRead(){
	if (hasSelect()){
		queryBox("确定标记为已读？", function(){
			var ids = [];
			var atgt = $("#J_all-list").find(":checkbox");
			atgt.each(function(){
				// 选择已经被选中的,拼接ids
				if (this.checked){
					var tgt = $(this).parents("tr:first");
					tgt.addClass("has-read");
					ids.push(tgt.data("data").id);
				}
			});
			
			// 发送ajax请求，标记为已读
			$.getJSON(adminPath + "/notice/ajax/signRead", {ids : ids.join(",")}, function(result){
				if (result.success){
					$("#J_check-all").attr("checked", false);
					optTips("操作成功", reGetList);
				}
				else {
					layer.msg(result.info);
				}
			});
		});
	}
}

// 删除
function delLetter(){
	if (hasSelect()){
		queryBox("确定删除？", function(){
			var ids = [];
	    	var atgt = $("#J_all-list").find(":checkbox");
	    	atgt.each(function(){
	    		// 选择已经被选中的,拼接ids
	    		if (this.checked){
	    			var tgt = $(this).parents("tr:first");
	    			ids.push(tgt.data("data").id);
	    		}
	    	});
	    	
	    	$.getJSON(adminPath + "/notice/ajax/del", {ids : ids.join(",")}, function(result){
	    		if (result.success){
	    			$("#J_check-all").attr("checked", false);
	    			optTips("删除成功", reGetList);
				}
				else {
					layer.msg(result.info);
				}
	    	});
		});
	}
}

// 点击一条信息后跳转到相应的详情,并标记为已读
function toDetail(){
	var tgt = getEvtTgt().parents("tr:first");
	var data = tgt.data("data");
	// 消息类型
	var contentType = data.contentType;
	// 来源ID
	var sourceId = data.sourceId;
	// 用户类型
	var userType = data.targetUserType;
	
	// 如果是已经是已读的，则不需要标记已读
	if (tgt.hasClass("has-read")){
		goDetail(contentType, sourceId, userType);
	}
	else {
		tgt.addClass("has-read");
		// 发送ajax请求，标记为已读
		$.getJSON(adminPath + "/notice/ajax/signRead", {ids : data.id}, function(result){
			if (result.success){
				goDetail(contentType, sourceId, userType);
			}
			else {
				layer.msg(result.info);
			}
		});
	}
}

// TODO:接受者类型，不同接收者，消息跳转到不同详情
function goDetail(contentType, sourceId, userType){
	/*console.log(userType)
	console.log(contentType)
	return;*/
	var url;
	// 买家
	if (userType == "buyer"){
		// 验苗申请受理，跳转到验苗申请的“验苗中”列表
		if (contentType == "validateSeedling"){
			url = adminPath + "/validateApply?status=verifing";
		}
		// 经纪提交验苗结果，跳转到验苗申请详情
		else if (contentType == "validateResultAudit"){
			url = adminPath + "/validateApply/itemdetail/" + sourceId + ".html";
		}
		// 经纪发货通知，跳转到发车信息列表
		else if (contentType == "sendLoadCar"){
			url = adminPath + "/loadcar?status=unreceipt";
		}
		// 退货申请，跳转到订单详情
		else if(contentType == "orderReturn"){
			url = adminPath + "/order/detail/" + sourceId + ".html";
		}
		// 审核采购单，跳转到采购详情
		else if (contentType == "auditPurchase"){
			url = adminPath + "/purchase/detail/" + sourceId + ".html";
		}
		// 卖家修改了订单价格修改，跳转到订单详情
		else if (contentType == "orderEditPrice"){
			url = adminPath + "/order/detail/" + sourceId + ".html";
		}
		// 买家线下付款，客服确认收款，跳转到订单“待收货”
		else if (contentType == "orderAssurePay"){
			url = adminPath + "/order?status=unreceipt";
		}
	}
	// 卖家
	else if (userType == "seller"){
		// 买家申请验苗，不跳转（卖家没有相关验苗的订单）
		if (contentType == "validateSeedling"){
			return;
		}
		// 苗木资源审核，跳转到苗木详情
		else if (contentType == "seedlingAudit"){
			url = adminPath + "/seedling/detail/" + sourceId + ".html";
		}
		// 苗木资源清场推荐，跳转到苗木商城
		else if (contentType == "seedlingSpecialRecommend"){
			url = basePath + "/market/index";
		}
		// 审核报价，跳转到报价详情
		else if (contentType == "auditQuote"){
			url = adminPath + "/quote/detail/" + sourceId + ".html";
		}
		// 采用报价，跳转到报价详情
		else if (contentType == "useQuote"){
			url = adminPath + "/quote/detail/" + sourceId + ".html";
		}
		// 订单创建，跳转销售订单的详情
		else if (contentType == "orderCreated"){
			url = adminPath + "/sales/order/detail/" + sourceId + ".html";
		}
		// 订单付款，跳转销售订单的详情
		else if (contentType == "orderAssurePay"){
			url = adminPath + "/sales/order/detail/" + sourceId + ".html";
		}
		// 订单创建，跳转到销售订单详情
		else if (contentType == "orderCreated"){
			url = adminPath + "/sales/order/detail/" + sourceId + ".html";
		}
		// 买家付款，跳转到销售订单详情
		else if (contentType == "orderPayed"){
			url = adminPath + "/sales/order/detail/" + sourceId + ".html";
		}
		// 买家确认收货（担保购，放心购的订单），跳转到销售订单详情
		else if (contentType == "orderTradeType"){
			url = adminPath + "/sales/order/detail/" + sourceId + ".html";
		}
	}
	// 经纪
	else if (userType == "agent"){
		// 验苗请求，跳转到验苗处理的“新验苗”列表
		if (contentType == "validateSeedling"){
			url = adminPath + "/agent/validateSeedling?status=accepted";
		}
		// 验苗结果审核，跳转到验苗处理详情
		else if (contentType == "validateResultAudit"){
			url = adminPath + "/agent/validateSeedling/itemdetail/" + sourceId + ".html";
		}
		// 客服创建发货单，跳转到订单发货详情
		else if (contentType == "createDelivery"){
			url = adminPath + "/agent/sendGoods/detail/" + sourceId + ".html";
		}
	}
	
	window.location.href = url;
}

// 判断是否有选中的
function hasSelect(){
	var flag = true;
	var atgt = $("#J_all-list").find(":checkbox");
	atgt.each(function(){
		if (this.checked){
			flag = false;
		}
	});
	
	// 如果都没有被选中，则显示提示
	if (flag){
		layer.msg("请至少选择一条通知");
		return false;
	}
	
	return true;
}

function initEvent(){
	// 切换状态
	$("#J_title-nav li").click(cutType);
}

// 切换消息通知类型，重新加载列表
function cutType(){
	var tgt = getEvtTgt().parents("li:first");
	var noticeType = tgt.attr("noticeType"); 
	if (noticeType == undefined) {
		tgt = getEvtTgt();
		noticeType = tgt.attr("noticeType");
	}
	
	$("#J_title-nav li").removeClass("active");
	tgt.addClass("active");
	
	$("#J_status").val(noticeType);
	reGetList();
}

// 重新获取列表信息，包括不同状态下的数量获取
function reGetList(){
	firstLoad = true;
	getTypeCount();
	getCount();
}

// 获取不同状态下的数量
function getTypeCount(){
	$.getJSON(adminPath + "/notice/ajax/typeCount?random=" + new Date().getTime(), $("#J_list-form").serialize(), function(result){
		if (result.success){
			var data = result.data.countMap;
			for(var key in data){
				var c = data[key];
				var liNav = $("#J_title-nav").find("li." + key);
				liNav.find(".c-span").remove();
				if (c > 0){
					liNav.find("a").append("<span class='c-span'>(<span class='red'>" + c + "</span>)</span>"); 
				}
			}
		}
		else {
			layer.msg(result.info);
		}
	});
}

// 关闭loading
function closeLoading(){
	layer.close(layIndex);
}