var SprintStatus = SuperWidget.extend({
	title: null,
	sprintId: 0,
	style: "panel-default",
	instanceId : null,
	urlUserDataStory : "/fluig-dev-web/public/story/status/",
	urlUserData : "/api/public/social/user/",
	urlUserPhoto : "/social/api/rest/social/image/profile/",
	photoSize : "/MEDIUM_PICTURE",
	aliasUser : null,
	isOwner : false,

	loadingStoryStatus : null,

	//metodo iniciado quando a widget é carregada
	init: function() {
		//Armazena a referência do script para obter as variáveis globais
		var that = this;

		/*
		 * Preenche principais dados do usuário
		 */

		var loadingStoryStatus = FLUIGC.loading(that.DOM);
		loadingStoryStatus.show();
		console.log(that.sprintId);
		var templates = {};
		if(that.sprintId==0 || that.sprintId==""){
			templates['template-sprint-status-error'] = $('.template-sprint-status-error').html().trim();
			var html = Mustache.render(templates['template-sprint-status-error']);

			$("#SprintStatus_" + that.instanceId).find("#sprintStatus").append(html);
			loadingStoryStatus.hide();
		}else{
			$.getJSON(this.urlUserDataStory+that.sprintId, function(result){


				templates['template-sprint-status'] = $('.template-sprint-status').html().trim();
				var html = Mustache.render(templates['template-sprint-status'], result);

				$("#SprintStatus_" + that.instanceId).find("#sprintStatus").append(html);

				loadingStoryStatus.hide();
			});
		}


	}
});
