<%@ tag pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!-- /daterangepicker/ -->
<script src="${root}/statics/plugins/daterangepicker/moment.min.js"></script>
<script src="${root}/statics/plugins/daterangepicker/daterangepicker.js"></script>
<script>
$('#reservation').daterangepicker({
    "locale": {
        "direction": "ltr",
        "format": "YYYY-MM-DD",
        "separator": " - ",
        "applyLabel": "确定",
        "cancelLabel": "取消",
        "fromLabel": "从",
        "toLabel": "到",
        "customRangeLabel": "Custom",
        "daysOfWeek": [
            "日",
            "一",
            "二",
            "三",
            "四",
            "五",
            "六"
        ],
        "monthNames": [
            "一",
            "二",
            "三",
            "四",
            "五",
            "六",
            "七",
            "八",
            "九",
            "十",
            "十一",
            "十二"
        ],
        "firstDay": 1
    },
    "linkedCalendars": false,
    "autoUpdateInput": false,
    "showCustomRangeLabel": false
}, function(start, end, label) {
	$('#reservation').val(start.format('YYYY/MM/DD')+"-"+end.format('YYYY/MM/DD'));
	var start = start.format('YYYY-MM-DD');
    var end = end.format('YYYY-MM-DD');
    param.start_date = start;
    param.end_date = end;
    table.settings()[0].ajax.data = param;
    table.ajax.reload();
});
</script>
