var SprintStatusEdit = SuperWidget.extend({

	instanceId: null,
	title: null,
	sprintId: 0,
	style: "panel-default",

	bindings: {
		local: {
			'form-edit':  ['submit_saveform'],
			'save-widget-config': ['click_updatePreferences']
		},
		global: {

		}
	},

	init: function() {
		$style = $('[data-widget-style]', this.DOM);
		$style.val(this.style);
	},
	
	saveForm: function(el, ev) {
		ev.preventDefault();
	},

	updatePreferences: function(el, ev) {
		var that = this,
		args = {},
		$title = $('[data-widget-title]', this.DOM),
		$sprintId = $('[data-widget-sprintId]', this.DOM),
		hasError =  null;

		var btn = $(el);
		btn.prop('disabled',true);

		if ($title.val() == '') {
			hasError = "${i18n.getTranslation('enter.title')}";
		}else{
			this.title = $title.val();
		}
		
		if ($sprintId.val() == '') {
			hasError = "${i18n.getTranslation('enter.sprintId')}";
		}else{
			this.sprintId = $sprintId.val();
		}
		
		this.style = $style.val();
		
		args['title'] = this.title;
		args['sprintId'] = this.sprintId;
		
		if(hasError==null) {
			this.rest(WCMSpaceAPI.PageService.UPDATEPREFERENCES,[that.instanceId, args],function(res) {
				if(res) {
					FLUIGC.toast({
						message: "${i18n.getTranslation('successs.update')}",
						type: "success"
					});
				}
				btn.prop('disabled',false);
			},
			function(xhr, text, errData) {
				btn.prop('disabled',false);
				FLUIGC.toast({
					message: errData.message,
					type: "danger"
				});

			}
			);
		}else{
			FLUIGC.toast({
				title: '${i18n.getTranslation("txt.warning")}',
				message: hasError,
				type: "warning"
			});

			btn.prop('disabled',false);
		}
	},

});
