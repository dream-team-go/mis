<%@ tag pageEncoding="UTF-8"%>
<%@ attribute name='pageCss'%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!-- Tell the browser to be responsive to screen width -->
<meta content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no" name="viewport">
<!-- Bootstrap 3.3.6 -->
<link rel="stylesheet" href="${root}/statics/bootstrap/css/bootstrap.css">
<!-- Font Awesome -->
<link rel="stylesheet" href="${root}/statics/AdminLTE/css/font-awesome.min.css">
<!-- Ionicons -->
<link rel="stylesheet" href="${root}/statics/AdminLTE/css/ionicons.min.css">
<!-- Theme style -->
<link rel="stylesheet" href="${root}/statics/AdminLTE/css/AdminLTE.min.css">
<link rel="stylesheet" href="${root}/statics/AdminLTE/css/skins/_all-skins.min.css">
<link rel="stylesheet" href="${root }/statics/nice-validator-1.0.0/dist/jquery.validator.css">
<style>
	#to_the_top {
		display: none;
		position: fixed;
		cursor: pointer;
		/* modify below css to your needs */
		background: url('${root}/statics/images/to_the_top.png') no-repeat left top;
		width: 40px;
		height: 40px;
		bottom: 80px;
		right: 40px;
		z-index: 9999;
	}
</style>
${pageCss }