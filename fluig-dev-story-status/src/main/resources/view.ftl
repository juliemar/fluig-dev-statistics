<#assign params = '{instanceId: ${instanceId?c}, title:"${title!}", style: "${style!}", sprintId: "${sprintId!}"}'?html>
<div id="StoryStatus_${instanceId}" class="fluig-style-guide wcm-widget-class super-widget"
	data-params="StoryStatus.instance(${params})">

	<div class="panel ${style!'panel-default'}">
		<div class="panel-heading">
			<h3 class="panel-title">${title!"${i18n.getTranslation('userdata.userdata')}"}
			</h3>
		</div>
		<div class="panel-body">
			<ul id="storyStatusList" class="list-group">
				<script class="template-story-status" type="x-tmpl-mustache">
					{{#content}}
					<li class="list-group-item">
						<span class="badge badge-{{style}}">{{totalIssuesOpen}}</span>
						<a href="{{ link }}" target="_blank">{{ name }}</a>
						<span class="label label-{{style}}">{{status}}</span>
					</li>
					{{/content}}
				</script>
				<script class="template-story-status-error" type="x-tmpl-mustache">
					
					<div class="alert alert-danger" role="alert">
						<strong>Ops!</strong> Você precisa definir o sprintId. Para definir edite a página e preencha o campo sprintId
					</div>
					
				</script>
			</ul>
		</div>
	</div>
</div>


