<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<div class="row">

	<div class="span9">
		<h1>
			<i class="icon-food"></i> Selectable food...
		</h1>
	</div>
	<div class="span3">
		<div class="pull-right">
			or&nbsp;&nbsp;&nbsp;<a href="event/create" class="btn btn-large btn-primary"> <i class="icon-edit"></i> <spring:message code="event.create" /></a>
		</div>
	</div>
</div>
<hr>

<div class="well">
	<div class="row-fluid">
		<div data-ng-switch data-on="event.type" class="span4" data-ng-repeat="event in homeEvents">
			<h3 data-ng-switch="EXTERNAL">{{event.location.name}}</h3>
			<p><span class="label label-info" data-ng-switch="EXTERNAL"><i class="icon-food"></i> go out eating to</span></p>
			<p><i class="icon-time"></i> {{fromNow(event.date)}}<p>
			<p data-ng-switch="EXTERNAL"><i class="icon-map-marker"></i>  {{event.location.address}}</p>
			<p>
				<i class="icon-user"></i>
				<gravatar-image data-email="event.creator.email" data-size="30" data-secure="true" ></gravatar-image> {{event.creator.username}}
			<p>
			<div>
				<button class="btn btn-small" >View details »</button>
				<button class="btn btn-small btn-primary">attend</button> 
			</div>
		</div>
	</div>
</div>

<!--/span-->
<!--/row-->