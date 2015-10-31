
<#attempt>
<#assign params='{instanceId:${instanceId?c},title:"${title!}", style:"${style!}",sprintId:"${sprintId!}"}'?html>
<div id="SprintStatus_${instanceId}" class="fluig-style-guide dash-theme wcm-widget-class super-widget"
	data-params="SprintStatus.instance(${params})">

	<div class="page-header">
		<div class="row">
			<div class="col-md-12">
				<h1 class="fs-no-margin fs-ellipsis fs-full-width">${title!"${i18n.getTranslation('userdata.userdata')}"}
				</h1>
			</div>
		</div>
	</div>
	<div id="sprintStatus">
		<script class="template-sprint-status" type="x-tmpl-mustache">
			{{#content}}
			<div class="row">
				<div class="col-lg-3 col-md-6">
					<div class="panel panel-primary">
						<div class="panel-heading">
							<div class="row">
								<div class="col-xs-3">
									<i
										class="fluigicon fluigicon-activity-list-pending fluigicon-thumbnail-lg"></i>
								</div>
								<div class="col-xs-9 text-right">
									<div class="huge">{{totalIssues}}</div>
									<div>Total de Tarefas!</div>
								</div>
							</div>
						</div>
					</div>
				</div>
				<div class="col-lg-3 col-md-6">
					<div class="panel panel-green">
						<div class="panel-heading">
							<div class="row">
								<div class="col-xs-3">
									<i class="fluigicon fluigicon-period-verified fluigicon-thumbnail-lg"></i>
								</div>
								<div class="col-xs-9 text-right">
									<div class="huge">{{totalIssuesClose}}</div>
									<div>Tarefas concluídas!</div>
								</div>
							</div>
						</div>
					</div>
				</div>
				<div class="col-lg-3 col-md-6">
					<div class="panel panel-yellow">
						<div class="panel-heading">
							<div class="row">
								<div class="col-xs-3">
									<i class="fluigicon fluigicon-period-transfer fluigicon-thumbnail-lg"></i>
								</div>
								<div class="col-xs-9 text-right">
									<div class="huge">{{totalIssuesProgress}}</div>
									<div>Tarefas em progresso</div>
								</div>
							</div>
						</div>
					</div>
				</div>
				<div class="col-lg-3 col-md-6">
					<div class="panel panel-red">
						<div class="panel-heading">
							<div class="row">
								<div class="col-xs-3">
									<i class="fluigicon fluigicon-period-remove fluigicon-thumbnail-lg"></i>
								</div>
								<div class="col-xs-9 text-right">
									<div class="huge">{{totalIssuesOpen}}</div>
									<div>Tarefas paradas</div>
								</div>
							</div>
						</div>
					</div>
				</div>
			</div>
			<div class="progress">
				<div class="progress-bar progress-bar-success" role="progressbar"
					aria-valuenow="{{sprintProgress}}" aria-valuemin="0" aria-valuemax="100"
					style="width: {{sprintProgress}}%;">
					{{sprintProgress}}% </div>
			</div>
	</div>
	{{/content}}
</script>

	<script class="template-sprint-status-error" type="x-tmpl-mustache">

		<div class="alert alert-danger" role="alert">
			<strong>Ops!</strong>
			Você precisa definir o sprintId. Para definir edite a página e
			preencha o campo sprintId
		</div>
	</script>
</div>
</div>
<#recover> <#include "/social_error.ftl"> </#attempt>
<!-- ISSO PRECISA SUMIR MAS AINDA NÃO ENTENDI PQ UMA WIDGET GERA CORRETO 
	E OUTRA NÃO -->
<script src="/sprintstatus/resources/js/sprintstatus-edit.js"
	type="text/javascript" async="" defer=""></script>
<script src="/sprintstatus/resources/js/sprintstatus.js" type="text/javascript"
	async="" defer=""></script>