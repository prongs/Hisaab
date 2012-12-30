define(["dojo/_base/declare","dojo/dom",
	"dijit/_WidgetBase", "dijit/_TemplatedMixin",
	"dojo/text!./templates/MainViewWidget.html",
	"dijit/layout/TabContainer", "dijit/layout/ContentPane"],
	function(declare, dom, WidgetBase, TemplatedMixin, template){
		return declare("Hisaab.MainViewWidget", [WidgetBase, TemplatedMixin], {
			templateString: template,
			constructor: function(){

			},
			startup: function(){
				
			}
		});
	});