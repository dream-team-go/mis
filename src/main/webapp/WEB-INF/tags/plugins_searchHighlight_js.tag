<%@ tag pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<script src="${root}/statics/plugins/jquery.highlight.js"></script>
<!-- <script src="//cdn.datatables.net/plug-ins/1.10.12/features/searchHighlight/dataTables.searchHighlight.min.js"></script>  -->
<script>
/*
SearchHighlight for DataTables v1.0.1
2014 SpryMedia Ltd - datatables.net/license
*/
(function(h,g,a){function e(d,c){d.unhighlight();c.rows({filter:"applied"}).data().length&&(c.columns().every(function(){this.nodes().flatten().to$().unhighlight({className:"column_highlight"});this.nodes().flatten().to$().highlight(a.trim(this.search()).split(/\s+/),{className:"column_highlight"})}),d.highlight(a.trim(c.search()).split(/\s+/)))}a(g).on("init.dt.dth",function(d,c){if("dt"===d.namespace){var b=new a.fn.dataTable.Api(c),f=a(b.table().body());if(a(b.table().node()).hasClass("searchHighlight")||
c.oInit.searchHighlight||a.fn.dataTable.defaults.searchHighlight)b.on("draw.dt.dth column-visibility.dt.dth column-reorder.dt.dth",function(){e(f,b)}).on("destroy",function(){b.off("draw.dt.dth column-visibility.dt.dth column-reorder.dt.dth")}),b.search()&&e(f,b)}})})(window,document,jQuery);
</script>