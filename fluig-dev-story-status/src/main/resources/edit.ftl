
<#attempt>
<#assign params = '{instanceId: ${instanceId?c}, title:"${title!}", style: "${style!}",sprintId: "${sprintId!}"}'?html>
<div id="StoryStatusEdit_${instanceId}" class="fluig-style-guide wcm-widget-class super-widget"
	data-params="StoryStatusEdit.instance(${params})">


	<div class="panel ${style!'panel-default'}">
		<div class="panel-heading">
			<h3 class="panel-title">${i18n.getTranslation('userdata.txt')}</h3>
		</div>
		<div class="panel-body">
			<form name="editPosterForm" data-form-edit>
				<div class="form-group">
					<label for="title">${i18n.getTranslation('userdata.widget.title')}</label>
					<input type="text" class="form-control" data-widget-title
						placeholder="${i18n.getTranslation('userdata.widget.title')}" value="${title!}">
				</div>
				
				<div class="form-group">
					<label for="title">${i18n.getTranslation('userdata.widget.sprintId')}</label>
					<input type="text" class="form-control" data-widget-sprintId
						placeholder="${i18n.getTranslation('userdata.widget.sprint')}" value="${sprintId!}">
				</div>
				<div class="form-group">
					<label for="title">${i18n.getTranslation('userdata.widget.style')}</label>
					<select class="form-control" data-widget-style >
						<option value="panel-default">${i18n.getTranslation('color.gray')}</option>
						<option value="panel-danger">${i18n.getTranslation('color.red')}</option>
						<option value="panel-warn">${i18n.getTranslation('color.yellow')}</option>
						<option value="panel-primary">${i18n.getTranslation('color.black')}</option>
						<option value="panel-info">${i18n.getTranslation('color.blue')}</option>
						<option value="panel-success">${i18n.getTranslation('color.green')}</option>
					</select>
				</div>

				<div class="pull-right">
					<button type="button" class="btn btn-primary"
						data-loading-text="Loading..." data-save-widget-config>${i18n.getTranslation('btn.save')}
					</button>
				</div>
			</form>
		</div>
	</div>
</div>
<#recover> <#include "/social_error.ftl"> </#attempt>
