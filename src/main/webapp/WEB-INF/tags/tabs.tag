<%@ tag pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<link rel="stylesheet" href="${root }/statics/plugins/tabs/demo.css">
<link rel="stylesheet" href="${root }/statics/plugins/tabs/tabs.css">
<link rel="stylesheet" href="${root }/statics/plugins/tabs/tabstyles.css">

<script src="${root }/statics/plugins/tabs/modernizr.custom.js"></script>
<script src="${root }/statics/plugins/tabs/cbpFWTabs.js"></script>
<script type="text/javascript">
	(function() {

		[].slice.call( document.querySelectorAll( '.tabs' ) ).forEach( function( el ) {
			new CBPFWTabs( el );
		});

	})();
</script>