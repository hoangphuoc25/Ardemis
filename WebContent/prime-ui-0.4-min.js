/*
 * Copyright 2009-2012 Prime Teknoloji.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
PUI={zindex:1000,scrollInView:function(b,e){var h=parseFloat(b.css("borderTopWidth"))||0,d=parseFloat(b.css("paddingTop"))||0,f=e.offset().top-b.offset().top-h-d,a=b.scrollTop(),c=b.height(),g=e.outerHeight(true);
if(f<0){b.scrollTop(a+f)
}else{if((f+g)>c){b.scrollTop(a+f-c+g)
}}}};$(function(){$.widget("prime-ui.puiaccordion",{options:{activeIndex:0,multiple:false},_create:function(){if(this.options.multiple){this.options.activeIndex=[]
}var a=this;
this.element.addClass("pui-accordion ui-widget ui-helper-reset");
this.element.children("h3").addClass("pui-accordion-header ui-helper-reset ui-state-default").each(function(c){var f=$(this),e=f.html(),d=(c==a.options.activeIndex)?"ui-state-active ui-corner-top":"ui-corner-all",b=(c==a.options.activeIndex)?"ui-icon ui-icon-triangle-1-s":"ui-icon ui-icon-triangle-1-e";
f.addClass(d).html('<span class="'+b+'"></span><a href="#">'+e+"</a>")
});
this.element.children("div").each(function(b){var c=$(this);
c.addClass("pui-accordion-content ui-helper-reset ui-widget-content");
if(b!=a.options.activeIndex){c.addClass("ui-helper-hidden")
}});
this.headers=this.element.children(".pui-accordion-header");
this.panels=this.element.children(".pui-accordion-content");
this.headers.children("a").disableSelection();
this._bindEvents()
},_bindEvents:function(){var a=this;
this.headers.mouseover(function(){var b=$(this);
if(!b.hasClass("ui-state-active")&&!b.hasClass("ui-state-disabled")){b.addClass("ui-state-hover")
}}).mouseout(function(){var b=$(this);
if(!b.hasClass("ui-state-active")&&!b.hasClass("ui-state-disabled")){b.removeClass("ui-state-hover")
}}).click(function(d){var c=$(this);
if(!c.hasClass("ui-state-disabled")){var b=c.index()/2;
if(c.hasClass("ui-state-active")){a.unselect(b)
}else{a.select(b)
}}d.preventDefault()
})
},select:function(b){var a=this.panels.eq(b);
this._trigger("change",a);
if(this.options.multiple){this._addToSelection(b)
}else{this.options.activeIndex=b
}this._show(a)
},unselect:function(b){var a=this.panels.eq(b),c=a.prev();
c.attr("aria-expanded",false).children(".ui-icon").removeClass("ui-icon-triangle-1-s").addClass("ui-icon-triangle-1-e");
c.removeClass("ui-state-active ui-corner-top").addClass("ui-corner-all");
a.attr("aria-hidden",true).slideUp();
this._removeFromSelection(b)
},_show:function(b){if(!this.options.multiple){var c=this.headers.filter(".ui-state-active");
c.children(".ui-icon").removeClass("ui-icon-triangle-1-s").addClass("ui-icon-triangle-1-e");
c.attr("aria-expanded",false).removeClass("ui-state-active ui-corner-top").addClass("ui-corner-all").next().attr("aria-hidden",true).slideUp()
}var a=b.prev();
a.attr("aria-expanded",true).addClass("ui-state-active ui-corner-top").removeClass("ui-state-hover ui-corner-all").children(".ui-icon").removeClass("ui-icon-triangle-1-e").addClass("ui-icon-triangle-1-s");
b.attr("aria-hidden",false).slideDown("normal")
},_addToSelection:function(a){this.options.activeIndex.push(a)
},_removeFromSelection:function(a){this.options.activeIndex=$.grep(this.options.activeIndex,function(b){return b!=a
})
}})
});$(function(){$.widget("prime-ui.puibutton",{options:{icon:null,iconPos:"left"},_create:function(){var b=this.element,d=b.text()||"pui-button",c=b.prop("disabled"),a=null;
if(this.options.icon){a=(d==="pui-button")?"pui-button-icon-only":"pui-button-text-icon-"+this.options.iconPos
}else{a="pui-button-text-only"
}if(c){a+=" ui-state-disabled"
}this.element.addClass("pui-button ui-widget ui-state-default ui-corner-all "+a).text("");
if(this.options.icon){this.element.append('<span class="pui-button-icon-'+this.options.iconPos+" ui-icon "+this.options.icon+'" />')
}this.element.append('<span class="pui-button-text">'+d+"</span>");
b.attr("role","button").attr("aria-disabled",c);
if(!c){this._bindEvents()
}},_bindEvents:function(){var a=this.element,b=this;
a.on("mouseover.puibutton",function(){if(!a.prop("disabled")){a.addClass("ui-state-hover")
}}).on("mouseout.puibutton",function(){$(this).removeClass("ui-state-active ui-state-hover")
}).on("mousedown.puibutton",function(){if(!a.hasClass("ui-state-disabled")){a.addClass("ui-state-active").removeClass("ui-state-hover")
}}).on("mouseup.puibutton",function(c){a.removeClass("ui-state-active").addClass("ui-state-hover");
b._trigger("click",c)
}).on("focus.puibutton",function(){a.addClass("ui-state-focus")
}).on("blur.puibutton",function(){a.removeClass("ui-state-focus")
}).on("keydown.puibutton",function(c){if(c.keyCode==$.ui.keyCode.SPACE||c.keyCode==$.ui.keyCode.ENTER||c.keyCode==$.ui.keyCode.NUMPAD_ENTER){a.addClass("ui-state-active")
}}).on("keyup.puibutton",function(){a.removeClass("ui-state-active")
});
return this
},_unbindEvents:function(){this.element.off("mouseover.puibutton mouseout.puibutton mousedown.puibutton mouseup.puibutton focus.puibutton blur.puibutton keydown.puibutton keyup.puibutton")
},disable:function(){this._unbindEvents();
this.element.addClass("ui-state-disabled")
},enable:function(){this._bindEvents();
this.element.removeClass("ui-state-disabled")
}})
});$(function(){$.widget("prime-ui.puifieldset",{options:{toggleable:false,toggleDuration:"normal",collapsed:false},_create:function(){this.element.addClass("pui-fieldset ui-widget ui-widget-content ui-corner-all").children("legend").addClass("pui-fieldset-legend ui-corner-all ui-state-default");
this.element.contents(":not(legend)").wrapAll('<div class="pui-fieldset-content" />');
this.legend=this.element.children("legend.pui-fieldset-legend");
this.content=this.element.children("div.pui-fieldset-content");
if(this.options.toggleable){this.element.addClass("pui-fieldset-toggleable");
this.toggler=$('<span class="pui-fieldset-toggler ui-icon" />').prependTo(this.legend);
this._bindEvents();
if(this.options.collapsed){this.content.hide();
this.toggler.addClass("ui-icon-plusthick")
}else{this.toggler.addClass("ui-icon-minusthick")
}}},_bindEvents:function(){var a=this;
this.legend.on("click.puifieldset",function(b){a.toggle(b)
}).on("mouseover.puifieldset",function(){a.legend.addClass("ui-state-hover")
}).on("mouseout.puifieldset",function(){a.legend.removeClass("ui-state-hover ui-state-active")
}).on("mousedown.puifieldset",function(){a.legend.removeClass("ui-state-hover").addClass("ui-state-active")
}).on("mouseup.puifieldset",function(){a.legend.removeClass("ui-state-active").addClass("ui-state-hover")
})
},toggle:function(b){var a=this;
this._trigger("beforeToggle",b);
if(this.options.collapsed){this.toggler.removeClass("ui-icon-plusthick").addClass("ui-icon-minusthick")
}else{this.toggler.removeClass("ui-icon-minusthick").addClass("ui-icon-plusthick")
}this.content.slideToggle(this.options.toggleSpeed,"easeInOutCirc",function(){a._trigger("afterToggle",b);
a.options.collapsed=!a.options.collapsed
})
}})
});$(function(){$.widget("prime-ui.puigrowl",{options:{sticky:false,life:3000},_create:function(){var a=this.element;
a.addClass("pui-growl ui-widget").appendTo(document.body)
},show:function(a){var b=this;
this.clear();
$.each(a,function(c,d){b._renderMessage(d)
})
},clear:function(){this.element.children("div.pui-growl-item-container").remove()
},_renderMessage:function(c){var a='<div class="pui-growl-item-container ui-state-highlight ui-corner-all ui-helper-hidden pui-shadow" aria-live="polite">';
a+='<div class="pui-growl-item">';
a+='<div class="pui-growl-icon-close ui-icon ui-icon-closethick" style="display:none"></div>';
a+='<span class="pui-growl-image pui-growl-image-'+c.severity+'" />';
a+='<div class="pui-growl-message">';
a+='<span class="pui-growl-title">'+c.summary+"</span>";
a+="<p>"+c.detail+"</p>";
a+='</div><div style="clear: both;"></div></div></div>';
var b=$(a);
this._bindMessageEvents(b);
b.appendTo(this.element).fadeIn()
},_removeMessage:function(a){a.fadeTo("normal",0,function(){a.slideUp("normal","easeInOutCirc",function(){a.remove()
})
})
},_bindMessageEvents:function(a){var c=this,b=this.options.sticky;
a.on("mouseover.puigrowl",function(){var d=$(this);
if(!d.is(":animated")){d.find("div.pui-growl-icon-close:first").show()
}}).on("mouseout.puigrowl",function(){$(this).find("div.pui-growl-icon-close:first").hide()
});
a.find("div.pui-growl-icon-close").on("click.puigrowl",function(){c._removeMessage(a);
if(!b){clearTimeout(a.data("timeout"))
}});
if(!b){this._setRemovalTimeout(a)
}},_setRemovalTimeout:function(a){var c=this;
var b=setTimeout(function(){c._removeMessage(a)
},this.options.life);
a.data("timeout",b)
}})
});$(function(){$.widget("prime-ui.puiinputtext",{_create:function(){var a=this.element,b=a.prop("disabled");
a.addClass("pui-inputtext ui-widget ui-state-default ui-corner-all");
if(b){a.addClass("ui-state-disabled")
}else{a.hover(function(){a.toggleClass("ui-state-hover")
}).focus(function(){a.addClass("ui-state-focus")
}).blur(function(){a.removeClass("ui-state-focus")
})
}a.attr("role","textbox").attr("aria-disabled",b).attr("aria-readonly",a.prop("readonly")).attr("aria-multiline",a.is("textarea"))
},_destroy:function(){}})
});$(function(){$.widget("prime-ui.puiinputtextarea",{options:{autoResize:false,autoComplete:false,maxlength:null,counter:null,counterTemplate:"{0}",minQueryLength:3,queryDelay:700},_create:function(){var a=this;
this.element.puiinputtext();
if(this.options.autoResize){this.options.rowsDefault=this.element.attr("rows");
this.options.colsDefault=this.element.attr("cols");
this.element.addClass("pui-inputtextarea-resizable");
this.element.keyup(function(){a._resize()
}).focus(function(){a._resize()
}).blur(function(){a._resize()
})
}if(this.options.maxlength){this.element.keyup(function(d){var c=a.element.val(),b=c.length;
if(b>a.options.maxlength){a.element.val(c.substr(0,a.options.maxlength))
}if(a.options.counter){a._updateCounter()
}})
}if(this.options.counter){this._updateCounter()
}if(this.options.autoComplete){this._initAutoComplete()
}},_updateCounter:function(){var d=this.element.val(),c=d.length;
if(this.options.counter){var b=this.options.maxlength-c,a=this.options.counterTemplate.replace("{0}",b);
this.options.counter.text(a)
}},_resize:function(){var d=0,a=this.element.val().split("\n");
for(var b=a.length-1;
b>=0;
--b){d+=Math.floor((a[b].length/this.options.colsDefault)+1)
}var c=(d>=this.options.rowsDefault)?(d+1):this.options.rowsDefault;
this.element.attr("rows",c)
},_initAutoComplete:function(){var b='<div id="'+this.id+'_panel" class="pui-autocomplete-panel ui-widget-content ui-corner-all ui-helper-hidden ui-shadow"></div>',c=this;
this.panel=$(b).appendTo(document.body);
this.element.keyup(function(g){var f=$.ui.keyCode;
switch(g.which){case f.UP:case f.LEFT:case f.DOWN:case f.RIGHT:case f.ENTER:case f.NUMPAD_ENTER:case f.TAB:case f.SPACE:case f.CONTROL:case f.ALT:case f.ESCAPE:case 224:break;
default:var d=c._extractQuery();
if(d&&d.length>=c.options.minQueryLength){if(c.timeout){c._clearTimeout(c.timeout)
}c.timeout=setTimeout(function(){c.search(d)
},c.options.queryDelay)
}break
}}).keydown(function(j){var d=c.panel.is(":visible"),i=$.ui.keyCode;
switch(j.which){case i.UP:case i.LEFT:if(d){var h=c.items.filter(".ui-state-highlight"),g=h.length==0?c.items.eq(0):h.prev();
if(g.length==1){h.removeClass("ui-state-highlight");
g.addClass("ui-state-highlight");
if(c.options.scrollHeight){PUI.scrollInView(c.panel,g)
}}j.preventDefault()
}else{c._clearTimeout()
}break;
case i.DOWN:case i.RIGHT:if(d){var h=c.items.filter(".ui-state-highlight"),f=h.length==0?_self.items.eq(0):h.next();
if(f.length==1){h.removeClass("ui-state-highlight");
f.addClass("ui-state-highlight");
if(c.options.scrollHeight){PUI.scrollInView(c.panel,f)
}}j.preventDefault()
}else{c._clearTimeout()
}break;
case i.ENTER:case i.NUMPAD_ENTER:if(d){c.items.filter(".ui-state-highlight").trigger("click");
j.preventDefault()
}else{c._clearTimeout()
}break;
case i.SPACE:case i.CONTROL:case i.ALT:case i.BACKSPACE:case i.ESCAPE:case 224:c._clearTimeout();
if(d){c._hide()
}break;
case i.TAB:c._clearTimeout();
if(d){c.items.filter(".ui-state-highlight").trigger("click");
c._hide()
}break
}});
$(document.body).bind("mousedown.puiinputtextarea",function(d){if(c.panel.is(":hidden")){return
}var f=c.panel.offset();
if(d.target===c.element.get(0)){return
}if(d.pageX<f.left||d.pageX>f.left+c.panel.width()||d.pageY<f.top||d.pageY>f.top+c.panel.height()){c._hide()
}});
var a="resize."+this.id;
$(window).unbind(a).bind(a,function(){if(c.panel.is(":visible")){c._hide()
}})
},_bindDynamicEvents:function(){var a=this;
this.items.bind("mouseover",function(){var b=$(this);
if(!b.hasClass("ui-state-highlight")){a.items.filter(".ui-state-highlight").removeClass("ui-state-highlight");
b.addClass("ui-state-highlight")
}}).bind("click",function(d){var c=$(this),e=c.attr("data-item-value"),b=e.substring(a.query.length);
a.element.focus();
a.element.insertText(b,a.element.getSelection().start,true);
a._hide();
a._trigger("itemselect",d,c)
})
},_clearTimeout:function(){if(this.timeout){clearTimeout(this.timeout)
}this.timeout=null
},_extractQuery:function(){var b=this.element.getSelection().end,a=/\S+$/.exec(this.element.get(0).value.slice(0,b)),c=a?a[0]:null;
return c
},search:function(b){this.query=b;
var a={query:b};
if(this.options.completeSource){this.options.completeSource.call(this,a,this._handleResponse)
}},_handleResponse:function(c){this.panel.html("");
var d=$('<ul class="pui-autocomplete-items pui-autocomplete-list ui-widget-content ui-widget ui-corner-all ui-helper-reset"></ul>');
for(var a=0;
a<c.length;
a++){var b=$('<li class="pui-autocomplete-item pui-autocomplete-list-item ui-corner-all"></li>');
b.attr("data-item-value",c[a].value);
b.text(c[a].label);
d.append(b)
}this.panel.append(d).show();
this.items=this.panel.find(".pui-autocomplete-item");
this._bindDynamicEvents();
if(this.items.length>0){this.items.eq(0).addClass("ui-state-highlight");
if(this.options.scrollHeight&&this.panel.height()>this.options.scrollHeight){this.panel.height(this.options.scrollHeight)
}if(this.panel.is(":hidden")){this._show()
}else{this._alignPanel()
}}else{this.panel.hide()
}},_alignPanel:function(){var b=this.element.getCaretPosition(),a=this.element.offset();
this.panel.css({left:a.left+b.left,top:a.top+b.top,width:this.element.innerWidth()})
},_show:function(){this._alignPanel();
this.panel.show()
},_hide:function(){this.panel.hide()
},_refresh:function(){},_destroy:function(){alert("destroy")
}})
});$(function(){$.widget("prime-ui.puilightbox",{options:{iframeWidth:640,iframeHeight:480,iframe:false},_create:function(){this.options.mode=this.options.iframe?"iframe":(this.element.children("div").length==1)?"inline":"image";
var a='<div class="pui-lightbox ui-widget ui-helper-hidden ui-corner-all pui-shadow">';
a+='<div class="pui-lightbox-content-wrapper">';
a+='<a class="ui-state-default pui-lightbox-nav-left ui-corner-right ui-helper-hidden"><span class="ui-icon ui-icon-carat-1-w">go</span></a>';
a+='<div class="pui-lightbox-content ui-corner-all"></div>';
a+='<a class="ui-state-default pui-lightbox-nav-right ui-corner-left ui-helper-hidden"><span class="ui-icon ui-icon-carat-1-e">go</span></a>';
a+="</div>";
a+='<div class="pui-lightbox-caption ui-widget-header"><span class="pui-lightbox-caption-text"></span>';
a+='<a class="pui-lightbox-close ui-corner-all" href="#"><span class="ui-icon ui-icon-closethick"></span></a><div style="clear:both" /></div>';
a+="</div>";
this.panel=$(a).appendTo(document.body);
this.contentWrapper=this.panel.children(".pui-lightbox-content-wrapper");
this.content=this.contentWrapper.children(".pui-lightbox-content");
this.caption=this.panel.children(".pui-lightbox-caption");
this.captionText=this.caption.children(".pui-lightbox-caption-text");
this.closeIcon=this.caption.children(".pui-lightbox-close");
if(this.options.mode==="image"){this._setupImaging()
}else{if(this.options.mode==="inline"){this._setupInline()
}else{if(this.options.mode==="iframe"){this._setupIframe()
}}}this._bindCommonEvents();
this.links.data("puilightbox-trigger",true).find("*").data("puilightbox-trigger",true);
this.closeIcon.data("puilightbox-trigger",true).find("*").data("puilightbox-trigger",true)
},_bindCommonEvents:function(){var a=this;
this.closeIcon.hover(function(){$(this).toggleClass("ui-state-hover")
}).click(function(b){a.hide();
b.preventDefault()
});
$(document.body).bind("click.pui-lightbox",function(c){if(a.isHidden()){return
}var b=$(c.target);
if(b.data("puilightbox-trigger")){return
}var d=a.panel.offset();
if(c.pageX<d.left||c.pageX>d.left+a.panel.width()||c.pageY<d.top||c.pageY>d.top+a.panel.height()){a.hide()
}});
$(window).resize(function(){if(!a.isHidden()){$(document.body).children(".ui-widget-overlay").css({width:$(document).width(),height:$(document).height()})
}})
},_setupImaging:function(){var a=this;
this.links=this.element.children("a");
this.content.append('<img class="ui-helper-hidden"></img>');
this.imageDisplay=this.content.children("img");
this.navigators=this.contentWrapper.children("a");
this.imageDisplay.load(function(){var d=$(this);
a._scaleImage(d);
var c=(a.panel.width()-d.width())/2,b=(a.panel.height()-d.height())/2;
a.content.removeClass("pui-lightbox-loading").animate({width:d.width(),height:d.height()},500,function(){d.fadeIn();
a._showNavigators();
a.caption.slideDown()
});
a.panel.animate({left:"+="+c,top:"+="+b},500)
});
this.navigators.hover(function(){$(this).toggleClass("ui-state-hover")
}).click(function(c){var d=$(this);
a._hideNavigators();
if(d.hasClass("pui-lightbox-nav-left")){var b=a.current==0?a.links.length-1:a.current-1;
a.links.eq(b).trigger("click")
}else{var b=a.current==a.links.length-1?0:a.current+1;
a.links.eq(b).trigger("click")
}c.preventDefault()
});
this.links.click(function(c){var b=$(this);
if(a.isHidden()){a.content.addClass("pui-lightbox-loading").width(32).height(32);
a.show()
}else{a.imageDisplay.fadeOut(function(){$(this).css({width:"auto",height:"auto"});
a.content.addClass("pui-lightbox-loading")
});
a.caption.slideUp()
}setTimeout(function(){a.imageDisplay.attr("src",b.attr("href"));
a.current=b.index();
var d=b.attr("title");
if(d){a.captionText.html(d)
}},1000);
c.preventDefault()
})
},_scaleImage:function(g){var f=$(window),c=f.width(),b=f.height(),d=g.width(),a=g.height(),e=a/d;
if(d>=c&&e<=1){d=c*0.75;
a=d*e
}else{if(a>=b){a=b*0.75;
d=a/e
}}g.css({width:d+"px",height:a+"px"})
},_setupInline:function(){this.links=this.element.children("a");
this.inline=this.element.children("div").addClass("pui-lightbox-inline");
this.inline.appendTo(this.content).show();
var a=this;
this.links.click(function(b){a.show();
var c=$(this).attr("title");
if(c){a.captionText.html(c);
a.caption.slideDown()
}b.preventDefault()
})
},_setupIframe:function(){var a=this;
this.links=this.element;
this.iframe=$('<iframe frameborder="0" style="width:'+this.options.iframeWidth+"px;height:"+this.options.iframeHeight+'px;border:0 none; display: block;"></iframe>').appendTo(this.content);
if(this.options.iframeTitle){this.iframe.attr("title",this.options.iframeTitle)
}this.element.click(function(b){if(!a.iframeLoaded){a.content.addClass("pui-lightbox-loading").css({width:a.options.iframeWidth,height:a.options.iframeHeight});
a.show();
a.iframe.on("load",function(){a.iframeLoaded=true;
a.content.removeClass("pui-lightbox-loading")
}).attr("src",a.element.attr("href"))
}else{a.show()
}var c=a.element.attr("title");
if(c){a.caption.html(c);
a.caption.slideDown()
}b.preventDefault()
})
},show:function(){this.center();
this.panel.css("z-index",++PUI.zindex).show();
if(!this.modality){this._enableModality()
}this._trigger("show")
},hide:function(){this.panel.fadeOut();
this._disableModality();
this.caption.hide();
if(this.options.mode==="image"){this.imageDisplay.hide().attr("src","").removeAttr("style");
this._hideNavigators()
}this._trigger("hide")
},center:function(){var c=$(window),b=(c.width()/2)-(this.panel.width()/2),a=(c.height()/2)-(this.panel.height()/2);
this.panel.css({left:b,top:a})
},_enableModality:function(){this.modality=$('<div class="ui-widget-overlay"></div>').css({width:$(document).width(),height:$(document).height(),"z-index":this.panel.css("z-index")-1}).appendTo(document.body)
},_disableModality:function(){this.modality.remove();
this.modality=null
},_showNavigators:function(){this.navigators.zIndex(this.imageDisplay.zIndex()+1).show()
},_hideNavigators:function(){this.navigators.hide()
},isHidden:function(){return this.panel.is(":hidden")
},showURL:function(a){if(a.width){this.iframe.attr("width",a.width)
}if(a.height){this.iframe.attr("height",a.height)
}this.iframe.attr("src",a.src);
this.show()
}})
});$(function(){$.widget("prime-ui.puipanel",{options:{toggleable:false,toggleDuration:"normal",toggleOrientation:"vertical",collapsed:false,closable:false,closeDuration:"normal"},_create:function(){this.element.addClass("pui-panel ui-widget ui-widget-content ui-corner-all").contents().wrapAll('<div class="pui-panel-content ui-widget-content" />');
var c=this.element.attr("title");
if(c){this.element.prepend('<div class="pui-panel-titlebar ui-widget-header ui-helper-clearfix ui-corner-all"><span class="ui-panel-title">'+c+"</span></div>").removeAttr("title")
}this.header=this.element.children("div.pui-panel-titlebar");
this.title=this.header.children("span.ui-panel-title");
this.content=this.element.children("div.pui-panel-content");
var b=this;
if(this.options.closable){this.closer=$('<a class="pui-panel-titlebar-icon ui-corner-all ui-state-default" href="#"><span class="ui-icon ui-icon-closethick"></span></a>').appendTo(this.header).on("click.puipanel",function(d){b.close();
d.preventDefault()
})
}if(this.options.toggleable){var a=this.options.collapsed?"ui-icon-plusthick":"ui-icon-minusthick";
this.toggler=$('<a class="pui-panel-titlebar-icon ui-corner-all ui-state-default" href="#"><span class="ui-icon '+a+'"></span></a>').appendTo(this.header).on("click.puipanel",function(d){b.toggle();
d.preventDefault()
});
if(this.options.collapsed){this.content.hide()
}}this._bindEvents()
},_bindEvents:function(){this.header.find("a.pui-panel-titlebar-icon").on("hover.puipanel",function(){$(this).toggleClass("ui-state-hover")
})
},close:function(){var a=this;
this._trigger("beforeClose",null);
this.element.fadeOut(this.options.closeDuration,function(){a._trigger("afterClose",null)
})
},toggle:function(){if(this.options.collapsed){this.expand()
}else{this.collapse()
}},expand:function(){this.toggler.children("span.ui-icon").removeClass("ui-icon-plusthick").addClass("ui-icon-minusthick");
if(this.options.toggleOrientation==="vertical"){this._slideDown()
}else{if(this.options.toggleOrientation==="horizontal"){this._slideRight()
}}},collapse:function(){this.toggler.children("span.ui-icon").removeClass("ui-icon-minusthick").addClass("ui-icon-plusthick");
if(this.options.toggleOrientation==="vertical"){this._slideUp()
}else{if(this.options.toggleOrientation==="horizontal"){this._slideLeft()
}}},_slideUp:function(){var a=this;
this._trigger("beforeCollapse");
this.content.slideUp(this.options.toggleDuration,"easeInOutCirc",function(){a._trigger("afterCollapse");
a.options.collapsed=!a.options.collapsed
})
},_slideDown:function(){var a=this;
this._trigger("beforeExpand");
this.content.slideDown(this.options.toggleDuration,"easeInOutCirc",function(){a._trigger("afterExpand");
a.options.collapsed=!a.options.collapsed
})
},_slideLeft:function(){var a=this;
this.originalWidth=this.element.width();
this.title.hide();
this.toggler.hide();
this.content.hide();
this.element.animate({width:"42px"},this.options.toggleSpeed,"easeInOutCirc",function(){a.toggler.show();
a.element.addClass("pui-panel-collapsed-h");
a.options.collapsed=!a.options.collapsed
})
},_slideRight:function(){var b=this,a=this.originalWidth||"100%";
this.toggler.hide();
this.element.animate({width:a},this.options.toggleSpeed,"easeInOutCirc",function(){b.element.removeClass("pui-panel-collapsed-h");
b.title.show();
b.toggler.show();
b.options.collapsed=!b.options.collapsed;
b.content.css({visibility:"visible",display:"block",height:"auto"})
})
}})
});$(function(){$.widget("prime-ui.puirating",{options:{stars:5,cancel:true},_create:function(){var b=this.element;
b.wrap("<div />");
this.container=b.parent();
this.container.addClass("pui-rating");
var d=b.val(),e=d==""?null:parseInt(d);
if(this.options.cancel){this.container.append('<div class="pui-rating-cancel"><a></a></div>')
}for(var c=0;
c<this.options.stars;
c++){var a=(e>c)?"pui-rating-star pui-rating-star-on":"pui-rating-star";
this.container.append('<div class="'+a+'"><a></a></div>')
}this.stars=this.container.children(".pui-rating-star");
if(b.prop("disabled")){this.container.addClass("ui-state-disabled")
}else{if(!b.prop("readonly")){this._bindEvents()
}}},_bindEvents:function(){var a=this;
this.stars.click(function(){var b=a.stars.index(this)+1;
a._setValue(b)
});
this.container.children(".pui-rating-cancel").hover(function(){$(this).toggleClass("pui-rating-cancel-hover")
}).click(function(){a.cancel()
})
},cancel:function(){this.element.val("");
this.stars.filter(".pui-rating-star-on").removeClass("pui-rating-star-on")
},_getValue:function(){var a=this.element.val();
return a==""?null:parseInt(a)
},_setValue:function(b){this.element.val(b);
this.stars.removeClass("pui-rating-star-on");
for(var a=0;
a<b;
a++){this.stars.eq(a).addClass("pui-rating-star-on")
}this._trigger("rate",null,b)
}})
});$(function(){$.widget("prime-ui.puispinner",{options:{step:1},_create:function(){var a=this.element,b=a.prop("disabled");
a.puiinputtext().addClass("pui-spinner-input").wrap('<span class="pui-spinner ui-widget ui-corner-all" />');
this.wrapper=a.parent();
this.wrapper.append('<a class="pui-spinner-button pui-spinner-up ui-corner-tr ui-button ui-widget ui-state-default ui-button-text-only"><span class="ui-button-text"><span class="ui-icon ui-icon-triangle-1-n"></span></span></a><a class="pui-spinner-button pui-spinner-down ui-corner-br ui-button ui-widget ui-state-default ui-button-text-only"><span class="ui-button-text"><span class="ui-icon ui-icon-triangle-1-s"></span></span></a>');
this.upButton=this.wrapper.children("a.pui-spinner-up");
this.downButton=this.wrapper.children("a.pui-spinner-down");
this._initValue();
if(!b&&!a.prop("readonly")){this._bindEvents()
}if(b){this.wrapper.addClass("ui-state-disabled")
}a.attr({role:"spinner","aria-multiline":false,"aria-valuenow":this.value});
if(this.options.min!=undefined){a.attr("aria-valuemin",this.options.min)
}if(this.options.max!=undefined){a.attr("aria-valuemax",this.options.max)
}if(a.prop("disabled")){a.attr("aria-disabled",true)
}if(a.prop("readonly")){a.attr("aria-readonly",true)
}},_bindEvents:function(){var a=this;
this.wrapper.children(".pui-spinner-button").mouseover(function(){$(this).addClass("ui-state-hover")
}).mouseout(function(){$(this).removeClass("ui-state-hover ui-state-active");
if(a.timer){clearInterval(a.timer)
}}).mouseup(function(){clearInterval(a.timer);
$(this).removeClass("ui-state-active").addClass("ui-state-hover")
}).mousedown(function(d){var c=$(this),b=c.hasClass("pui-spinner-up")?1:-1;
c.removeClass("ui-state-hover").addClass("ui-state-active");
if(a.element.is(":not(:focus)")){a.element.focus()
}a._repeat(null,b);
d.preventDefault()
});
this.element.keydown(function(c){var b=$.ui.keyCode;
switch(c.which){case b.UP:a._spin(a.options.step);
break;
case b.DOWN:a._spin(-1*a.options.step);
break;
default:break
}}).keyup(function(){a._updateValue()
}).blur(function(){a._format()
}).focus(function(){a.element.val(a.value)
});
this.element.bind("mousewheel",function(b,c){if(a.element.is(":focus")){if(c>0){a._spin(a.options.step)
}else{a._spin(-1*a.options.step)
}return false
}})
},_repeat:function(a,b){var d=this,c=a||500;
clearTimeout(this.timer);
this.timer=setTimeout(function(){d._repeat(40,b)
},c);
this._spin(this.options.step*b)
},_spin:function(a){var b=this.value+a;
if(this.options.min!=undefined&&b<this.options.min){b=this.cfg.min
}if(this.options.max!=undefined&&b>this.options.max){b=this.cfg.max
}this.element.val(b).attr("aria-valuenow",b);
this.value=b;
this.element.trigger("change")
},_updateValue:function(){var a=this.element.val();
if(a==""){if(this.options.min!=undefined){this.value=this.options.min
}else{this.value=0
}}else{if(this.options.step){a=parseFloat(a)
}else{a=parseInt(a)
}if(!isNaN(a)){this.value=a
}}},_initValue:function(){var a=this.element.val();
if(a==""){if(this.options.min!=undefined){this.value=this.options.min
}else{this.value=0
}}else{if(this.options.prefix){a=a.split(this.options.prefix)[1]
}if(this.options.suffix){a=a.split(this.options.suffix)[0]
}if(this.options.step){this.value=parseFloat(a)
}else{this.value=parseInt(a)
}}},_format:function(){var a=this.value;
if(this.options.prefix){a=this.options.prefix+a
}if(this.options.suffix){a=a+this.options.suffix
}this.element.val(a)
}})
});$(function(){$.widget("prime-ui.puitabview",{options:{activeIndex:0,orientation:"top"},_create:function(){var a=this.element;
a.addClass("pui-tabview ui-widget ui-widget-content ui-corner-all ui-hidden-container").children("ul").addClass("pui-tabview-nav ui-helper-reset ui-helper-clearfix ui-widget-header ui-corner-all").children("li").addClass("ui-state-default ui-corner-top");
a.addClass("pui-tabview-"+this.options.orientation);
a.children("div").addClass("pui-tabview-panels").children().addClass("pui-tabview-panel ui-widget-content ui-corner-bottom");
a.find("> ul.pui-tabview-nav > li").eq(this.options.activeIndex).addClass("pui-tabview-selected ui-state-active");
a.find("> div.pui-tabview-panels > div.pui-tabview-panel:not(:eq("+this.options.activeIndex+"))").addClass("ui-helper-hidden");
this.navContainer=a.children(".pui-tabview-nav");
this.panelContainer=a.children(".pui-tabview-panels");
this._bindEvents()
},_bindEvents:function(){var a=this;
this.navContainer.children("li").bind("mouseover.tabview",function(c){var b=$(this);
if(!b.hasClass("ui-state-disabled")){b.addClass("ui-state-hover")
}}).bind("mouseout.tabview",function(c){var b=$(this);
if(!b.hasClass("ui-state-disabled")){b.removeClass("ui-state-hover")
}}).bind("click.tabview",function(d){var c=$(this);
if($(d.target).is(":not(.ui-icon-close)")){var b=c.index();
if(!c.hasClass("ui-state-disabled")&&b!=a.options.selected){a.select(b)
}}d.preventDefault()
});
this.navContainer.find("li .ui-icon-close").bind("click.tabview",function(c){var b=$(this).parent().index();
a.remove(b);
c.preventDefault()
})
},select:function(c){this.options.selected=c;
var b=this.panelContainer.children().eq(c),g=this.navContainer.children(),f=g.filter(".ui-state-active"),a=g.eq(b.index()),e=this.panelContainer.children(".pui-tabview-panel:visible"),d=this;
e.attr("aria-hidden",true);
f.attr("aria-expanded",false);
b.attr("aria-hidden",false);
a.attr("aria-expanded",true);
if(this.options.effect){e.hide(this.options.effect.name,null,this.options.effect.duration,function(){f.removeClass("ui-state-focus pui-tabview-selected ui-state-active");
a.addClass("ui-state-focus pui-tabview-selected ui-state-active");
b.show(d.options.name,null,d.options.effect.duration,function(){d._trigger("change",null,c)
})
})
}else{f.removeClass("ui-state-focus pui-tabview-selected ui-state-active");
e.hide();
a.addClass("ui-state-focus pui-tabview-selected ui-state-active");
b.show();
this._trigger("change",null,c)
}},remove:function(b){var d=this.navContainer.children().eq(b),a=this.panelContainer.children().eq(b);
this._trigger("close",null,b);
d.remove();
a.remove();
if(b==this.options.selected){var c=this.options.selected==this.getLength()?this.options.selected-1:this.options.selected;
this.select(c)
}},getLength:function(){return this.navContainer.children().length
},getActiveIndex:function(){return this.options.selected
},_markAsLoaded:function(a){a.data("loaded",true)
},_isLoaded:function(a){return a.data("loaded")==true
},disable:function(a){this.navContainer.children().eq(a).addClass("ui-state-disabled")
},enable:function(a){this.navContainer.children().eq(a).removeClass("ui-state-disabled")
}})
});