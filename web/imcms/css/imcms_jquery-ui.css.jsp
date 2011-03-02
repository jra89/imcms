<%@ page

	contentType="text/css"
	pageEncoding="UTF-8"
	
%>
/*
 * imCMS version of: https://ajax.googleapis.com/ajax/libs/jqueryui/1.8.4/themes/redmond/jquery-ui.css
 * Has .imcmsAdmin.ui-... before everything, so it won't collide with site's jquery-ui.css
 * So it needs the imcmsAdmin class on every feature.
 * <%-- Generate in imcms4_admin.css.jsp --%>
 */

	
/*
 * jQuery UI CSS Framework @VERSION
 *
 * Copyright 2010, AUTHORS.txt (http://jqueryui.com/about)
 * Dual licensed under the MIT or GPL Version 2 licenses.
 * http://jquery.org/license
 *
 * http://docs.jquery.com/UI/Theming/API
 */

/* Layout helpers
----------------------------------*/
.imcmsAdmin.ui-helper-hidden { display: none; }
.imcmsAdmin.ui-helper-hidden-accessible { position: absolute; left: -99999999px; }
.imcmsAdmin.ui-helper-reset { margin: 0; padding: 0; border: 0; outline: 0; line-height: 1.3; text-decoration: none; font-size: 100%; list-style: none; }
.imcmsAdmin.ui-helper-clearfix:after { content: "."; display: block; height: 0; clear: both; visibility: hidden; }
.imcmsAdmin.ui-helper-clearfix { display: inline-block; }
/* required comment for clearfix to work in Opera \*/
* html .ui-helper-clearfix { height:1%; }
.imcmsAdmin.ui-helper-clearfix { display:block; }
/* end clearfix */
.imcmsAdmin.ui-helper-zfix { width: 100%; height: 100%; top: 0; left: 0; position: absolute; opacity: 0; filter:Alpha(Opacity=0); }


/* Interaction Cues
----------------------------------*/
.imcmsAdmin.ui-state-disabled { cursor: default !important; }


/* Icons
----------------------------------*/

/* states and images */
.imcmsAdmin.ui-icon { display: block; text-indent: -99999px; overflow: hidden; background-repeat: no-repeat; }


/* Misc visuals
----------------------------------*/

/* Overlays */
.imcmsAdmin.ui-widget-overlay { position: absolute; top: 0; left: 0; width: 100%; height: 100%; }


/*
 * jQuery UI CSS Framework @VERSION
 *
 * Copyright 2010, AUTHORS.txt (http://jqueryui.com/about)
 * Dual licensed under the MIT or GPL Version 2 licenses.
 * http://jquery.org/license
 *
 * http://docs.jquery.com/UI/Theming/API
 *
 * To view and modify this theme, visit http://jqueryui.com/themeroller/?ffDefault=Lucida%20Grande,%20Lucida%20Sans,%20Arial,%20sans-serif&fwDefault=bold&fsDefault=1.1em&cornerRadius=5px&bgColorHeader=5c9ccc&bgTextureHeader=12_gloss_wave.png&bgImgOpacityHeader=55&borderColorHeader=4297d7&fcHeader=ffffff&iconColorHeader=d8e7f3&bgColorContent=fcfdfd&bgTextureContent=06_inset_hard.png&bgImgOpacityContent=100&borderColorContent=a6c9e2&fcContent=222222&iconColorContent=469bdd&bgColorDefault=dfeffc&bgTextureDefault=02_glass.png&bgImgOpacityDefault=85&borderColorDefault=c5dbec&fcDefault=2e6e9e&iconColorDefault=6da8d5&bgColorHover=d0e5f5&bgTextureHover=02_glass.png&bgImgOpacityHover=75&borderColorHover=79b7e7&fcHover=1d5987&iconColorHover=217bc0&bgColorActive=f5f8f9&bgTextureActive=06_inset_hard.png&bgImgOpacityActive=100&borderColorActive=79b7e7&fcActive=e17009&iconColorActive=f9bd01&bgColorHighlight=fbec88&bgTextureHighlight=01_flat.png&bgImgOpacityHighlight=55&borderColorHighlight=fad42e&fcHighlight=363636&iconColorHighlight=2e83ff&bgColorError=fef1ec&bgTextureError=02_glass.png&bgImgOpacityError=95&borderColorError=cd0a0a&fcError=cd0a0a&iconColorError=cd0a0a&bgColorOverlay=aaaaaa&bgTextureOverlay=01_flat.png&bgImgOpacityOverlay=0&opacityOverlay=30&bgColorShadow=aaaaaa&bgTextureShadow=01_flat.png&bgImgOpacityShadow=0&opacityShadow=30&thicknessShadow=8px&offsetTopShadow=-8px&offsetLeftShadow=-8px&cornerRadiusShadow=8px
 */


/* Component containers
----------------------------------*/
.imcmsAdmin.ui-widget { font-family: Lucida Grande, Lucida Sans, Arial, sans-serif; font-size: 1.1em; }
.imcmsAdmin.ui-widget .ui-widget { font-size: 1em; }
.imcmsAdmin.ui-widget input, .ui-widget select, .ui-widget textarea, .ui-widget button { font-family: Lucida Grande, Lucida Sans, Arial, sans-serif; font-size: 1em; }
.imcmsAdmin.ui-widget-content { border: 1px solid #a6c9e2; background: #fcfdfd url(images/ui-bg_inset-hard_100_fcfdfd_1x100.png) 50% bottom repeat-x; color: #222222; }
.imcmsAdmin.ui-widget-content a { color: #222222; }
.imcmsAdmin.ui-widget-header { border: 1px solid #4297d7; background: #5c9ccc url(images/ui-bg_gloss-wave_55_5c9ccc_500x100.png) 50% 50% repeat-x; color: #ffffff; font-weight: bold; }
.imcmsAdmin.ui-widget-header a { color: #ffffff; }

/* Interaction states
----------------------------------*/
.imcmsAdmin.ui-state-default, .ui-widget-content .ui-state-default, .ui-widget-header .ui-state-default { border: 1px solid #c5dbec; background: #dfeffc url(images/ui-bg_glass_85_dfeffc_1x400.png) 50% 50% repeat-x; font-weight: bold; color: #2e6e9e; }
.imcmsAdmin.ui-state-default a, .ui-state-default a:link, .ui-state-default a:visited { color: #2e6e9e; text-decoration: none; }
.imcmsAdmin.ui-state-hover, .ui-widget-content .ui-state-hover, .ui-widget-header .ui-state-hover, .ui-state-focus, .ui-widget-content .ui-state-focus, .ui-widget-header .ui-state-focus { border: 1px solid #79b7e7; background: #d0e5f5 url(images/ui-bg_glass_75_d0e5f5_1x400.png) 50% 50% repeat-x; font-weight: bold; color: #1d5987; }
.imcmsAdmin.ui-state-hover a, .ui-state-hover a:hover { color: #1d5987; text-decoration: none; }
.imcmsAdmin.ui-state-active, .ui-widget-content .ui-state-active, .ui-widget-header .ui-state-active { border: 1px solid #79b7e7; background: #f5f8f9 url(images/ui-bg_inset-hard_100_f5f8f9_1x100.png) 50% 50% repeat-x; font-weight: bold; color: #e17009; }
.imcmsAdmin.ui-state-active a, .ui-state-active a:link, .ui-state-active a:visited { color: #e17009; text-decoration: none; }
.imcmsAdmin.ui-widget :active { outline: none; }

/* Interaction Cues
----------------------------------*/
.imcmsAdmin.ui-state-highlight, .ui-widget-content .ui-state-highlight, .ui-widget-header .ui-state-highlight  {border: 1px solid #fad42e; background: #fbec88 url(images/ui-bg_flat_55_fbec88_40x100.png) 50% 50% repeat-x; color: #363636; }
.imcmsAdmin.ui-state-highlight a, .ui-widget-content .ui-state-highlight a,.ui-widget-header .ui-state-highlight a { color: #363636; }
.imcmsAdmin.ui-state-error, .ui-widget-content .ui-state-error, .ui-widget-header .ui-state-error {border: 1px solid #cd0a0a; background: #fef1ec url(images/ui-bg_glass_95_fef1ec_1x400.png) 50% 50% repeat-x; color: #cd0a0a; }
.imcmsAdmin.ui-state-error a, .ui-widget-content .ui-state-error a, .ui-widget-header .ui-state-error a { color: #cd0a0a; }
.imcmsAdmin.ui-state-error-text, .ui-widget-content .ui-state-error-text, .ui-widget-header .ui-state-error-text { color: #cd0a0a; }
.imcmsAdmin.ui-priority-primary, .ui-widget-content .ui-priority-primary, .ui-widget-header .ui-priority-primary { font-weight: bold; }
.imcmsAdmin.ui-priority-secondary, .ui-widget-content .ui-priority-secondary,  .ui-widget-header .ui-priority-secondary { opacity: .7; filter:Alpha(Opacity=70); font-weight: normal; }
.imcmsAdmin.ui-state-disabled, .ui-widget-content .ui-state-disabled, .ui-widget-header .ui-state-disabled { opacity: .35; filter:Alpha(Opacity=35); background-image: none; }

/* Icons
----------------------------------*/

/* states and images */
.imcmsAdmin.ui-icon { width: 16px; height: 16px; background-image: url(images/ui-icons_469bdd_256x240.png); }
.imcmsAdmin.ui-widget-content .ui-icon {background-image: url(images/ui-icons_469bdd_256x240.png); }
.imcmsAdmin.ui-widget-header .ui-icon {background-image: url(images/ui-icons_d8e7f3_256x240.png); }
.imcmsAdmin.ui-state-default .ui-icon { background-image: url(images/ui-icons_6da8d5_256x240.png); }
.imcmsAdmin.ui-state-hover .ui-icon, .ui-state-focus .ui-icon {background-image: url(images/ui-icons_217bc0_256x240.png); }
.imcmsAdmin.ui-state-active .ui-icon {background-image: url(images/ui-icons_f9bd01_256x240.png); }
.imcmsAdmin.ui-state-highlight .ui-icon {background-image: url(images/ui-icons_2e83ff_256x240.png); }
.imcmsAdmin.ui-state-error .ui-icon, .ui-state-error-text .ui-icon {background-image: url(images/ui-icons_cd0a0a_256x240.png); }

/* positioning */
.imcmsAdmin.ui-icon-carat-1-n { background-position: 0 0; }
.imcmsAdmin.ui-icon-carat-1-ne { background-position: -16px 0; }
.imcmsAdmin.ui-icon-carat-1-e { background-position: -32px 0; }
.imcmsAdmin.ui-icon-carat-1-se { background-position: -48px 0; }
.imcmsAdmin.ui-icon-carat-1-s { background-position: -64px 0; }
.imcmsAdmin.ui-icon-carat-1-sw { background-position: -80px 0; }
.imcmsAdmin.ui-icon-carat-1-w { background-position: -96px 0; }
.imcmsAdmin.ui-icon-carat-1-nw { background-position: -112px 0; }
.imcmsAdmin.ui-icon-carat-2-n-s { background-position: -128px 0; }
.imcmsAdmin.ui-icon-carat-2-e-w { background-position: -144px 0; }
.imcmsAdmin.ui-icon-triangle-1-n { background-position: 0 -16px; }
.imcmsAdmin.ui-icon-triangle-1-ne { background-position: -16px -16px; }
.imcmsAdmin.ui-icon-triangle-1-e { background-position: -32px -16px; }
.imcmsAdmin.ui-icon-triangle-1-se { background-position: -48px -16px; }
.imcmsAdmin.ui-icon-triangle-1-s { background-position: -64px -16px; }
.imcmsAdmin.ui-icon-triangle-1-sw { background-position: -80px -16px; }
.imcmsAdmin.ui-icon-triangle-1-w { background-position: -96px -16px; }
.imcmsAdmin.ui-icon-triangle-1-nw { background-position: -112px -16px; }
.imcmsAdmin.ui-icon-triangle-2-n-s { background-position: -128px -16px; }
.imcmsAdmin.ui-icon-triangle-2-e-w { background-position: -144px -16px; }
.imcmsAdmin.ui-icon-arrow-1-n { background-position: 0 -32px; }
.imcmsAdmin.ui-icon-arrow-1-ne { background-position: -16px -32px; }
.imcmsAdmin.ui-icon-arrow-1-e { background-position: -32px -32px; }
.imcmsAdmin.ui-icon-arrow-1-se { background-position: -48px -32px; }
.imcmsAdmin.ui-icon-arrow-1-s { background-position: -64px -32px; }
.imcmsAdmin.ui-icon-arrow-1-sw { background-position: -80px -32px; }
.imcmsAdmin.ui-icon-arrow-1-w { background-position: -96px -32px; }
.imcmsAdmin.ui-icon-arrow-1-nw { background-position: -112px -32px; }
.imcmsAdmin.ui-icon-arrow-2-n-s { background-position: -128px -32px; }
.imcmsAdmin.ui-icon-arrow-2-ne-sw { background-position: -144px -32px; }
.imcmsAdmin.ui-icon-arrow-2-e-w { background-position: -160px -32px; }
.imcmsAdmin.ui-icon-arrow-2-se-nw { background-position: -176px -32px; }
.imcmsAdmin.ui-icon-arrowstop-1-n { background-position: -192px -32px; }
.imcmsAdmin.ui-icon-arrowstop-1-e { background-position: -208px -32px; }
.imcmsAdmin.ui-icon-arrowstop-1-s { background-position: -224px -32px; }
.imcmsAdmin.ui-icon-arrowstop-1-w { background-position: -240px -32px; }
.imcmsAdmin.ui-icon-arrowthick-1-n { background-position: 0 -48px; }
.imcmsAdmin.ui-icon-arrowthick-1-ne { background-position: -16px -48px; }
.imcmsAdmin.ui-icon-arrowthick-1-e { background-position: -32px -48px; }
.imcmsAdmin.ui-icon-arrowthick-1-se { background-position: -48px -48px; }
.imcmsAdmin.ui-icon-arrowthick-1-s { background-position: -64px -48px; }
.imcmsAdmin.ui-icon-arrowthick-1-sw { background-position: -80px -48px; }
.imcmsAdmin.ui-icon-arrowthick-1-w { background-position: -96px -48px; }
.imcmsAdmin.ui-icon-arrowthick-1-nw { background-position: -112px -48px; }
.imcmsAdmin.ui-icon-arrowthick-2-n-s { background-position: -128px -48px; }
.imcmsAdmin.ui-icon-arrowthick-2-ne-sw { background-position: -144px -48px; }
.imcmsAdmin.ui-icon-arrowthick-2-e-w { background-position: -160px -48px; }
.imcmsAdmin.ui-icon-arrowthick-2-se-nw { background-position: -176px -48px; }
.imcmsAdmin.ui-icon-arrowthickstop-1-n { background-position: -192px -48px; }
.imcmsAdmin.ui-icon-arrowthickstop-1-e { background-position: -208px -48px; }
.imcmsAdmin.ui-icon-arrowthickstop-1-s { background-position: -224px -48px; }
.imcmsAdmin.ui-icon-arrowthickstop-1-w { background-position: -240px -48px; }
.imcmsAdmin.ui-icon-arrowreturnthick-1-w { background-position: 0 -64px; }
.imcmsAdmin.ui-icon-arrowreturnthick-1-n { background-position: -16px -64px; }
.imcmsAdmin.ui-icon-arrowreturnthick-1-e { background-position: -32px -64px; }
.imcmsAdmin.ui-icon-arrowreturnthick-1-s { background-position: -48px -64px; }
.imcmsAdmin.ui-icon-arrowreturn-1-w { background-position: -64px -64px; }
.imcmsAdmin.ui-icon-arrowreturn-1-n { background-position: -80px -64px; }
.imcmsAdmin.ui-icon-arrowreturn-1-e { background-position: -96px -64px; }
.imcmsAdmin.ui-icon-arrowreturn-1-s { background-position: -112px -64px; }
.imcmsAdmin.ui-icon-arrowrefresh-1-w { background-position: -128px -64px; }
.imcmsAdmin.ui-icon-arrowrefresh-1-n { background-position: -144px -64px; }
.imcmsAdmin.ui-icon-arrowrefresh-1-e { background-position: -160px -64px; }
.imcmsAdmin.ui-icon-arrowrefresh-1-s { background-position: -176px -64px; }
.imcmsAdmin.ui-icon-arrow-4 { background-position: 0 -80px; }
.imcmsAdmin.ui-icon-arrow-4-diag { background-position: -16px -80px; }
.imcmsAdmin.ui-icon-extlink { background-position: -32px -80px; }
.imcmsAdmin.ui-icon-newwin { background-position: -48px -80px; }
.imcmsAdmin.ui-icon-refresh { background-position: -64px -80px; }
.imcmsAdmin.ui-icon-shuffle { background-position: -80px -80px; }
.imcmsAdmin.ui-icon-transfer-e-w { background-position: -96px -80px; }
.imcmsAdmin.ui-icon-transferthick-e-w { background-position: -112px -80px; }
.imcmsAdmin.ui-icon-folder-collapsed { background-position: 0 -96px; }
.imcmsAdmin.ui-icon-folder-open { background-position: -16px -96px; }
.imcmsAdmin.ui-icon-document { background-position: -32px -96px; }
.imcmsAdmin.ui-icon-document-b { background-position: -48px -96px; }
.imcmsAdmin.ui-icon-note { background-position: -64px -96px; }
.imcmsAdmin.ui-icon-mail-closed { background-position: -80px -96px; }
.imcmsAdmin.ui-icon-mail-open { background-position: -96px -96px; }
.imcmsAdmin.ui-icon-suitcase { background-position: -112px -96px; }
.imcmsAdmin.ui-icon-comment { background-position: -128px -96px; }
.imcmsAdmin.ui-icon-person { background-position: -144px -96px; }
.imcmsAdmin.ui-icon-print { background-position: -160px -96px; }
.imcmsAdmin.ui-icon-trash { background-position: -176px -96px; }
.imcmsAdmin.ui-icon-locked { background-position: -192px -96px; }
.imcmsAdmin.ui-icon-unlocked { background-position: -208px -96px; }
.imcmsAdmin.ui-icon-bookmark { background-position: -224px -96px; }
.imcmsAdmin.ui-icon-tag { background-position: -240px -96px; }
.imcmsAdmin.ui-icon-home { background-position: 0 -112px; }
.imcmsAdmin.ui-icon-flag { background-position: -16px -112px; }
.imcmsAdmin.ui-icon-calendar { background-position: -32px -112px; }
.imcmsAdmin.ui-icon-cart { background-position: -48px -112px; }
.imcmsAdmin.ui-icon-pencil { background-position: -64px -112px; }
.imcmsAdmin.ui-icon-clock { background-position: -80px -112px; }
.imcmsAdmin.ui-icon-disk { background-position: -96px -112px; }
.imcmsAdmin.ui-icon-calculator { background-position: -112px -112px; }
.imcmsAdmin.ui-icon-zoomin { background-position: -128px -112px; }
.imcmsAdmin.ui-icon-zoomout { background-position: -144px -112px; }
.imcmsAdmin.ui-icon-search { background-position: -160px -112px; }
.imcmsAdmin.ui-icon-wrench { background-position: -176px -112px; }
.imcmsAdmin.ui-icon-gear { background-position: -192px -112px; }
.imcmsAdmin.ui-icon-heart { background-position: -208px -112px; }
.imcmsAdmin.ui-icon-star { background-position: -224px -112px; }
.imcmsAdmin.ui-icon-link { background-position: -240px -112px; }
.imcmsAdmin.ui-icon-cancel { background-position: 0 -128px; }
.imcmsAdmin.ui-icon-plus { background-position: -16px -128px; }
.imcmsAdmin.ui-icon-plusthick { background-position: -32px -128px; }
.imcmsAdmin.ui-icon-minus { background-position: -48px -128px; }
.imcmsAdmin.ui-icon-minusthick { background-position: -64px -128px; }
.imcmsAdmin.ui-icon-close { background-position: -80px -128px; }
.imcmsAdmin.ui-icon-closethick { background-position: -96px -128px; }
.imcmsAdmin.ui-icon-key { background-position: -112px -128px; }
.imcmsAdmin.ui-icon-lightbulb { background-position: -128px -128px; }
.imcmsAdmin.ui-icon-scissors { background-position: -144px -128px; }
.imcmsAdmin.ui-icon-clipboard { background-position: -160px -128px; }
.imcmsAdmin.ui-icon-copy { background-position: -176px -128px; }
.imcmsAdmin.ui-icon-contact { background-position: -192px -128px; }
.imcmsAdmin.ui-icon-image { background-position: -208px -128px; }
.imcmsAdmin.ui-icon-video { background-position: -224px -128px; }
.imcmsAdmin.ui-icon-script { background-position: -240px -128px; }
.imcmsAdmin.ui-icon-alert { background-position: 0 -144px; }
.imcmsAdmin.ui-icon-info { background-position: -16px -144px; }
.imcmsAdmin.ui-icon-notice { background-position: -32px -144px; }
.imcmsAdmin.ui-icon-help { background-position: -48px -144px; }
.imcmsAdmin.ui-icon-check { background-position: -64px -144px; }
.imcmsAdmin.ui-icon-bullet { background-position: -80px -144px; }
.imcmsAdmin.ui-icon-radio-off { background-position: -96px -144px; }
.imcmsAdmin.ui-icon-radio-on { background-position: -112px -144px; }
.imcmsAdmin.ui-icon-pin-w { background-position: -128px -144px; }
.imcmsAdmin.ui-icon-pin-s { background-position: -144px -144px; }
.imcmsAdmin.ui-icon-play { background-position: 0 -160px; }
.imcmsAdmin.ui-icon-pause { background-position: -16px -160px; }
.imcmsAdmin.ui-icon-seek-next { background-position: -32px -160px; }
.imcmsAdmin.ui-icon-seek-prev { background-position: -48px -160px; }
.imcmsAdmin.ui-icon-seek-end { background-position: -64px -160px; }
.imcmsAdmin.ui-icon-seek-start { background-position: -80px -160px; }
/* ui-icon-seek-first is deprecated, use ui-icon-seek-start instead */
.imcmsAdmin.ui-icon-seek-first { background-position: -80px -160px; }
.imcmsAdmin.ui-icon-stop { background-position: -96px -160px; }
.imcmsAdmin.ui-icon-eject { background-position: -112px -160px; }
.imcmsAdmin.ui-icon-volume-off { background-position: -128px -160px; }
.imcmsAdmin.ui-icon-volume-on { background-position: -144px -160px; }
.imcmsAdmin.ui-icon-power { background-position: 0 -176px; }
.imcmsAdmin.ui-icon-signal-diag { background-position: -16px -176px; }
.imcmsAdmin.ui-icon-signal { background-position: -32px -176px; }
.imcmsAdmin.ui-icon-battery-0 { background-position: -48px -176px; }
.imcmsAdmin.ui-icon-battery-1 { background-position: -64px -176px; }
.imcmsAdmin.ui-icon-battery-2 { background-position: -80px -176px; }
.imcmsAdmin.ui-icon-battery-3 { background-position: -96px -176px; }
.imcmsAdmin.ui-icon-circle-plus { background-position: 0 -192px; }
.imcmsAdmin.ui-icon-circle-minus { background-position: -16px -192px; }
.imcmsAdmin.ui-icon-circle-close { background-position: -32px -192px; }
.imcmsAdmin.ui-icon-circle-triangle-e { background-position: -48px -192px; }
.imcmsAdmin.ui-icon-circle-triangle-s { background-position: -64px -192px; }
.imcmsAdmin.ui-icon-circle-triangle-w { background-position: -80px -192px; }
.imcmsAdmin.ui-icon-circle-triangle-n { background-position: -96px -192px; }
.imcmsAdmin.ui-icon-circle-arrow-e { background-position: -112px -192px; }
.imcmsAdmin.ui-icon-circle-arrow-s { background-position: -128px -192px; }
.imcmsAdmin.ui-icon-circle-arrow-w { background-position: -144px -192px; }
.imcmsAdmin.ui-icon-circle-arrow-n { background-position: -160px -192px; }
.imcmsAdmin.ui-icon-circle-zoomin { background-position: -176px -192px; }
.imcmsAdmin.ui-icon-circle-zoomout { background-position: -192px -192px; }
.imcmsAdmin.ui-icon-circle-check { background-position: -208px -192px; }
.imcmsAdmin.ui-icon-circlesmall-plus { background-position: 0 -208px; }
.imcmsAdmin.ui-icon-circlesmall-minus { background-position: -16px -208px; }
.imcmsAdmin.ui-icon-circlesmall-close { background-position: -32px -208px; }
.imcmsAdmin.ui-icon-squaresmall-plus { background-position: -48px -208px; }
.imcmsAdmin.ui-icon-squaresmall-minus { background-position: -64px -208px; }
.imcmsAdmin.ui-icon-squaresmall-close { background-position: -80px -208px; }
.imcmsAdmin.ui-icon-grip-dotted-vertical { background-position: 0 -224px; }
.imcmsAdmin.ui-icon-grip-dotted-horizontal { background-position: -16px -224px; }
.imcmsAdmin.ui-icon-grip-solid-vertical { background-position: -32px -224px; }
.imcmsAdmin.ui-icon-grip-solid-horizontal { background-position: -48px -224px; }
.imcmsAdmin.ui-icon-gripsmall-diagonal-se { background-position: -64px -224px; }
.imcmsAdmin.ui-icon-grip-diagonal-se { background-position: -80px -224px; }


/* Misc visuals
----------------------------------*/

/* Corner radius */
.imcmsAdmin.ui-corner-tl { -moz-border-radius-topleft: 5px; -webkit-border-top-left-radius: 5px; border-top-left-radius: 5px; }
.imcmsAdmin.ui-corner-tr { -moz-border-radius-topright: 5px; -webkit-border-top-right-radius: 5px; border-top-right-radius: 5px; }
.imcmsAdmin.ui-corner-bl { -moz-border-radius-bottomleft: 5px; -webkit-border-bottom-left-radius: 5px; border-bottom-left-radius: 5px; }
.imcmsAdmin.ui-corner-br { -moz-border-radius-bottomright: 5px; -webkit-border-bottom-right-radius: 5px; border-bottom-right-radius: 5px; }
.imcmsAdmin.ui-corner-top { -moz-border-radius-topleft: 5px; -webkit-border-top-left-radius: 5px; border-top-left-radius: 5px; -moz-border-radius-topright: 5px; -webkit-border-top-right-radius: 5px; border-top-right-radius: 5px; }
.imcmsAdmin.ui-corner-bottom { -moz-border-radius-bottomleft: 5px; -webkit-border-bottom-left-radius: 5px; border-bottom-left-radius: 5px; -moz-border-radius-bottomright: 5px; -webkit-border-bottom-right-radius: 5px; border-bottom-right-radius: 5px; }
.imcmsAdmin.ui-corner-right {  -moz-border-radius-topright: 5px; -webkit-border-top-right-radius: 5px; border-top-right-radius: 5px; -moz-border-radius-bottomright: 5px; -webkit-border-bottom-right-radius: 5px; border-bottom-right-radius: 5px; }
.imcmsAdmin.ui-corner-left { -moz-border-radius-topleft: 5px; -webkit-border-top-left-radius: 5px; border-top-left-radius: 5px; -moz-border-radius-bottomleft: 5px; -webkit-border-bottom-left-radius: 5px; border-bottom-left-radius: 5px; }
.imcmsAdmin.ui-corner-all { -moz-border-radius: 5px; -webkit-border-radius: 5px; border-radius: 5px; }

/* Overlays */
.imcmsAdmin.ui-widget-overlay { background: #aaaaaa url(images/ui-bg_flat_0_aaaaaa_40x100.png) 50% 50% repeat-x; opacity: .30;filter:Alpha(Opacity=30); }
.imcmsAdmin.ui-widget-shadow { margin: -8px 0 0 -8px; padding: 8px; background: #aaaaaa url(images/ui-bg_flat_0_aaaaaa_40x100.png) 50% 50% repeat-x; opacity: .30;filter:Alpha(Opacity=30); -moz-border-radius: 8px; -webkit-border-radius: 8px; border-radius: 8px; }/*
 * jQuery UI Resizable @VERSION
 *
 * Copyright 2010, AUTHORS.txt (http://jqueryui.com/about)
 * Dual licensed under the MIT or GPL Version 2 licenses.
 * http://jquery.org/license
 *
 * http://docs.jquery.com/UI/Resizable#theming
 */
.imcmsAdmin.ui-resizable { position: relative;}
.imcmsAdmin.ui-resizable-handle { position: absolute;font-size: 0.1px;z-index: 99999; display: block;}
.imcmsAdmin.ui-resizable-disabled .ui-resizable-handle, .ui-resizable-autohide .ui-resizable-handle { display: none; }
.imcmsAdmin.ui-resizable-n { cursor: n-resize; height: 7px; width: 100%; top: -5px; left: 0; }
.imcmsAdmin.ui-resizable-s { cursor: s-resize; height: 7px; width: 100%; bottom: -5px; left: 0; }
.imcmsAdmin.ui-resizable-e { cursor: e-resize; width: 7px; right: -5px; top: 0; height: 100%; }
.imcmsAdmin.ui-resizable-w { cursor: w-resize; width: 7px; left: -5px; top: 0; height: 100%; }
.imcmsAdmin.ui-resizable-se { cursor: se-resize; width: 12px; height: 12px; right: 1px; bottom: 1px; }
.imcmsAdmin.ui-resizable-sw { cursor: sw-resize; width: 9px; height: 9px; left: -5px; bottom: -5px; }
.imcmsAdmin.ui-resizable-nw { cursor: nw-resize; width: 9px; height: 9px; left: -5px; top: -5px; }
.imcmsAdmin.ui-resizable-ne { cursor: ne-resize; width: 9px; height: 9px; right: -5px; top: -5px;}/*
 * jQuery UI Selectable @VERSION
 *
 * Copyright 2010, AUTHORS.txt (http://jqueryui.com/about)
 * Dual licensed under the MIT or GPL Version 2 licenses.
 * http://jquery.org/license
 *
 * http://docs.jquery.com/UI/Selectable#theming
 */
.imcmsAdmin.ui-selectable-helper { position: absolute; z-index: 100; border:1px dotted black; }
/*
 * jQuery UI Accordion @VERSION
 *
 * Copyright 2010, AUTHORS.txt (http://jqueryui.com/about)
 * Dual licensed under the MIT or GPL Version 2 licenses.
 * http://jquery.org/license
 *
 * http://docs.jquery.com/UI/Accordion#theming
 */
/* IE/Win - Fix animation bug - #4615 */
.imcmsAdmin.ui-accordion { width: 100%; }
.imcmsAdmin.ui-accordion .ui-accordion-header { cursor: pointer; position: relative; margin-top: 1px; zoom: 1; }
.imcmsAdmin.ui-accordion .ui-accordion-li-fix { display: inline; }
.imcmsAdmin.ui-accordion .ui-accordion-header-active { border-bottom: 0 !important; }
.imcmsAdmin.ui-accordion .ui-accordion-header a { display: block; font-size: 1em; padding: .5em .5em .5em .7em; }
.imcmsAdmin.ui-accordion-icons .ui-accordion-header a { padding-left: 2.2em; }
.imcmsAdmin.ui-accordion .ui-accordion-header .ui-icon { position: absolute; left: .5em; top: 50%; margin-top: -8px; }
.imcmsAdmin.ui-accordion .ui-accordion-content { padding: 1em 2.2em; border-top: 0; margin-top: -2px; position: relative; top: 1px; margin-bottom: 2px; overflow: auto; display: none; zoom: 1; }
.imcmsAdmin.ui-accordion .ui-accordion-content-active { display: block; }/*
 * jQuery UI Autocomplete @VERSION
 *
 * Copyright 2010, AUTHORS.txt (http://jqueryui.com/about)
 * Dual licensed under the MIT or GPL Version 2 licenses.
 * http://jquery.org/license
 *
 * http://docs.jquery.com/UI/Autocomplete#theming
 */
.imcmsAdmin.ui-autocomplete { position: absolute; cursor: default; }	

/* workarounds */
* html .ui-autocomplete { width:1px; } /* without this, the menu expands to 100% in IE6 */

/*
 * jQuery UI Menu @VERSION
 *
 * Copyright 2010, AUTHORS.txt (http://jqueryui.com/about)
 * Dual licensed under the MIT or GPL Version 2 licenses.
 * http://jquery.org/license
 *
 * http://docs.jquery.com/UI/Menu#theming
 */
.imcmsAdmin.ui-menu {
	list-style:none;
	padding: 2px;
	margin: 0;
	display:block;
	float: left;
}
.imcmsAdmin.ui-menu .ui-menu {
	margin-top: -3px;
}
.imcmsAdmin.ui-menu .ui-menu-item {
	margin:0;
	padding: 0;
	zoom: 1;
	float: left;
	clear: left;
	width: 100%;
}
.imcmsAdmin.ui-menu .ui-menu-item a {
	text-decoration:none;
	display:block;
	padding:.2em .4em;
	line-height:1.5;
	zoom:1;
}
.imcmsAdmin.ui-menu .ui-menu-item a.ui-state-hover,
.imcmsAdmin.ui-menu .ui-menu-item a.ui-state-active {
	font-weight: normal;
	margin: -1px;
}
/*
 * jQuery UI Button @VERSION
 *
 * Copyright 2010, AUTHORS.txt (http://jqueryui.com/about)
 * Dual licensed under the MIT or GPL Version 2 licenses.
 * http://jquery.org/license
 *
 * http://docs.jquery.com/UI/Button#theming
 */
.imcmsAdmin.ui-button { display: inline-block; position: relative; padding: 0; margin-right: .1em; text-decoration: none !important; cursor: pointer; text-align: center; zoom: 1; overflow: visible; } /* the overflow property removes extra width in IE */
.imcmsAdmin.ui-button-icon-only { width: 2.2em; } /* to make room for the icon, a width needs to be set here */
button.ui-button-icon-only { width: 2.4em; } /* button elements seem to need a little more width */
.imcmsAdmin.ui-button-icons-only { width: 3.4em; } 
button.ui-button-icons-only { width: 3.7em; } 

/*button text element */
.imcmsAdmin.ui-button .ui-button-text { display: block; line-height: 1.4;  }
.imcmsAdmin.ui-button-text-only .ui-button-text { padding: .4em 1em; }
.imcmsAdmin.ui-button-icon-only .ui-button-text, .ui-button-icons-only .ui-button-text { padding: .4em; text-indent: -9999999px; }
.imcmsAdmin.ui-button-text-icon-primary .ui-button-text, .ui-button-text-icons .ui-button-text { padding: .4em 1em .4em 2.1em; }
.imcmsAdmin.ui-button-text-icon-secondary .ui-button-text, .ui-button-text-icons .ui-button-text { padding: .4em 2.1em .4em 1em; }
.imcmsAdmin.ui-button-text-icons .ui-button-text { padding-left: 2.1em; padding-right: 2.1em; }
/* no icon support for input elements, provide padding by default */
input.ui-button { padding: .4em 1em; }

/*button icon element(s) */
.imcmsAdmin.ui-button-icon-only .ui-icon, .ui-button-text-icon-primary .ui-icon, .ui-button-text-icon-secondary .ui-icon, .ui-button-text-icons .ui-icon, .ui-button-icons-only .ui-icon { position: absolute; top: 50%; margin-top: -8px; }
.imcmsAdmin.ui-button-icon-only .ui-icon { left: 50%; margin-left: -8px; }
.imcmsAdmin.ui-button-text-icon-primary .ui-button-icon-primary, .ui-button-text-icons .ui-button-icon-primary, .ui-button-icons-only .ui-button-icon-primary { left: .5em; }
.imcmsAdmin.ui-button-text-icon-secondary .ui-button-icon-secondary, .ui-button-text-icons .ui-button-icon-secondary, .ui-button-icons-only .ui-button-icon-secondary { right: .5em; }
.imcmsAdmin.ui-button-text-icons .ui-button-icon-secondary, .ui-button-icons-only .ui-button-icon-secondary { right: .5em; }

/*button sets*/
.imcmsAdmin.ui-buttonset { margin-right: 7px; }
.imcmsAdmin.ui-buttonset .ui-button { margin-left: 0; margin-right: -.3em; }

/* workarounds */
button.ui-button::-moz-focus-inner { border: 0; padding: 0; } /* reset extra padding in Firefox */
/*
 * jQuery UI Dialog @VERSION
 *
 * Copyright 2010, AUTHORS.txt (http://jqueryui.com/about)
 * Dual licensed under the MIT or GPL Version 2 licenses.
 * http://jquery.org/license
 *
 * http://docs.jquery.com/UI/Dialog#theming
 */
.imcmsAdmin.ui-dialog { position: absolute; padding: .2em; width: 300px; overflow: hidden; }
.imcmsAdmin.ui-dialog .ui-dialog-titlebar { padding: .5em 1em .3em; position: relative;  }
.imcmsAdmin.ui-dialog .ui-dialog-title { float: left; margin: .1em 16px .2em 0; } 
.imcmsAdmin.ui-dialog .ui-dialog-titlebar-close { position: absolute; right: .3em; top: 50%; width: 19px; margin: -10px 0 0 0; padding: 1px; height: 18px; }
.imcmsAdmin.ui-dialog .ui-dialog-titlebar-close span { display: block; margin: 1px; }
.imcmsAdmin.ui-dialog .ui-dialog-titlebar-close:hover, .ui-dialog .ui-dialog-titlebar-close:focus { padding: 0; }
.imcmsAdmin.ui-dialog .ui-dialog-content { position: relative; border: 0; padding: .5em 1em; background: none; overflow: auto; zoom: 1; }
.imcmsAdmin.ui-dialog .ui-dialog-buttonpane { text-align: left; border-width: 1px 0 0 0; background-image: none; margin: .5em 0 0 0; padding: .3em 1em .5em .4em; }
.imcmsAdmin.ui-dialog .ui-dialog-buttonpane .ui-dialog-buttonset { float: right; }
.imcmsAdmin.ui-dialog .ui-dialog-buttonpane button { margin: .5em .4em .5em 0; cursor: pointer; }
.imcmsAdmin.ui-dialog .ui-resizable-se { width: 14px; height: 14px; right: 3px; bottom: 3px; }
.imcmsAdmin.ui-draggable .ui-dialog-titlebar { cursor: move; }
/*
 * jQuery UI Slider @VERSION
 *
 * Copyright 2010, AUTHORS.txt (http://jqueryui.com/about)
 * Dual licensed under the MIT or GPL Version 2 licenses.
 * http://jquery.org/license
 *
 * http://docs.jquery.com/UI/Slider#theming
 */
.imcmsAdmin.ui-slider { position: relative; text-align: left; }
.imcmsAdmin.ui-slider .ui-slider-handle { position: absolute; z-index: 2; width: 1.2em; height: 1.2em; cursor: default; }
.imcmsAdmin.ui-slider .ui-slider-range { position: absolute; z-index: 1; font-size: .7em; display: block; border: 0; background-position: 0 0; }

.imcmsAdmin.ui-slider-horizontal { height: .8em; }
.imcmsAdmin.ui-slider-horizontal .ui-slider-handle { top: -.3em; margin-left: -.6em; }
.imcmsAdmin.ui-slider-horizontal .ui-slider-range { top: 0; height: 100%; }
.imcmsAdmin.ui-slider-horizontal .ui-slider-range-min { left: 0; }
.imcmsAdmin.ui-slider-horizontal .ui-slider-range-max { right: 0; }

.imcmsAdmin.ui-slider-vertical { width: .8em; height: 100px; }
.imcmsAdmin.ui-slider-vertical .ui-slider-handle { left: -.3em; margin-left: 0; margin-bottom: -.6em; }
.imcmsAdmin.ui-slider-vertical .ui-slider-range { left: 0; width: 100%; }
.imcmsAdmin.ui-slider-vertical .ui-slider-range-min { bottom: 0; }
.imcmsAdmin.ui-slider-vertical .ui-slider-range-max { top: 0; }/*
 * jQuery UI Tabs @VERSION
 *
 * Copyright 2010, AUTHORS.txt (http://jqueryui.com/about)
 * Dual licensed under the MIT or GPL Version 2 licenses.
 * http://jquery.org/license
 *
 * http://docs.jquery.com/UI/Tabs#theming
 */
.imcmsAdmin.ui-tabs { position: relative; padding: .2em; zoom: 1; } /* position: relative prevents IE scroll bug (element with position: relative inside container with overflow: auto appear as "fixed") */
.imcmsAdmin.ui-tabs .ui-tabs-nav { margin: 0; padding: .2em .2em 0; }
.imcmsAdmin.ui-tabs .ui-tabs-nav li { list-style: none; float: left; position: relative; top: 1px; margin: 0 .2em 1px 0; border-bottom: 0 !important; padding: 0; white-space: nowrap; }
.imcmsAdmin.ui-tabs .ui-tabs-nav li a { float: left; padding: .5em 1em; text-decoration: none; }
.imcmsAdmin.ui-tabs .ui-tabs-nav li.ui-tabs-selected { margin-bottom: 0; padding-bottom: 1px; }
.imcmsAdmin.ui-tabs .ui-tabs-nav li.ui-tabs-selected a, .ui-tabs .ui-tabs-nav li.ui-state-disabled a, .ui-tabs .ui-tabs-nav li.ui-state-processing a { cursor: text; }
.imcmsAdmin.ui-tabs .ui-tabs-nav li a, .ui-tabs.ui-tabs-collapsible .ui-tabs-nav li.ui-tabs-selected a { cursor: pointer; } /* first selector in group seems obsolete, but required to overcome bug in Opera applying cursor: text overall if defined elsewhere... */
.imcmsAdmin.ui-tabs .ui-tabs-panel { display: block; border-width: 0; padding: 1em 1.4em; background: none; }
.imcmsAdmin.ui-tabs .ui-tabs-hide { display: none !important; }
/*
 * jQuery UI Datepicker @VERSION
 *
 * Copyright 2010, AUTHORS.txt (http://jqueryui.com/about)
 * Dual licensed under the MIT or GPL Version 2 licenses.
 * http://jquery.org/license
 *
 * http://docs.jquery.com/UI/Datepicker#theming
 */
.imcmsAdmin.ui-datepicker { width: 17em; padding: .2em .2em 0; }
.imcmsAdmin.ui-datepicker .ui-datepicker-header { position:relative; padding:.2em 0; }
.imcmsAdmin.ui-datepicker .ui-datepicker-prev, .ui-datepicker .ui-datepicker-next { position:absolute; top: 2px; width: 1.8em; height: 1.8em; }
.imcmsAdmin.ui-datepicker .ui-datepicker-prev-hover, .ui-datepicker .ui-datepicker-next-hover { top: 1px; }
.imcmsAdmin.ui-datepicker .ui-datepicker-prev { left:2px; }
.imcmsAdmin.ui-datepicker .ui-datepicker-next { right:2px; }
.imcmsAdmin.ui-datepicker .ui-datepicker-prev-hover { left:1px; }
.imcmsAdmin.ui-datepicker .ui-datepicker-next-hover { right:1px; }
.imcmsAdmin.ui-datepicker .ui-datepicker-prev span, .ui-datepicker .ui-datepicker-next span { display: block; position: absolute; left: 50%; margin-left: -8px; top: 50%; margin-top: -8px;  }
.imcmsAdmin.ui-datepicker .ui-datepicker-title { margin: 0 2.3em; line-height: 1.8em; text-align: center; }
.imcmsAdmin.ui-datepicker .ui-datepicker-title select { font-size:1em; margin:1px 0; }
.imcmsAdmin.ui-datepicker select.ui-datepicker-month-year {width: 100%;}
.imcmsAdmin.ui-datepicker select.ui-datepicker-month, 
.imcmsAdmin.ui-datepicker select.ui-datepicker-year { width: 49%;}
.imcmsAdmin.ui-datepicker table {width: 100%; font-size: .9em; border-collapse: collapse; margin:0 0 .4em; }
.imcmsAdmin.ui-datepicker th { padding: .7em .3em; text-align: center; font-weight: bold; border: 0;  }
.imcmsAdmin.ui-datepicker td { border: 0; padding: 1px; }
.imcmsAdmin.ui-datepicker td span, .ui-datepicker td a { display: block; padding: .2em; text-align: right; text-decoration: none; }
.imcmsAdmin.ui-datepicker .ui-datepicker-buttonpane { background-image: none; margin: .7em 0 0 0; padding:0 .2em; border-left: 0; border-right: 0; border-bottom: 0; }
.imcmsAdmin.ui-datepicker .ui-datepicker-buttonpane button { float: right; margin: .5em .2em .4em; cursor: pointer; padding: .2em .6em .3em .6em; width:auto; overflow:visible; }
.imcmsAdmin.ui-datepicker .ui-datepicker-buttonpane button.ui-datepicker-current { float:left; }

/* with multiple calendars */
.imcmsAdmin.ui-datepicker.ui-datepicker-multi { width:auto; }
.imcmsAdmin.ui-datepicker-multi .ui-datepicker-group { float:left; }
.imcmsAdmin.ui-datepicker-multi .ui-datepicker-group table { width:95%; margin:0 auto .4em; }
.imcmsAdmin.ui-datepicker-multi-2 .ui-datepicker-group { width:50%; }
.imcmsAdmin.ui-datepicker-multi-3 .ui-datepicker-group { width:33.3%; }
.imcmsAdmin.ui-datepicker-multi-4 .ui-datepicker-group { width:25%; }
.imcmsAdmin.ui-datepicker-multi .ui-datepicker-group-last .ui-datepicker-header { border-left-width:0; }
.imcmsAdmin.ui-datepicker-multi .ui-datepicker-group-middle .ui-datepicker-header { border-left-width:0; }
.imcmsAdmin.ui-datepicker-multi .ui-datepicker-buttonpane { clear:left; }
.imcmsAdmin.ui-datepicker-row-break { clear:both; width:100%; }

/* RTL support */
.imcmsAdmin.ui-datepicker-rtl { direction: rtl; }
.imcmsAdmin.ui-datepicker-rtl .ui-datepicker-prev { right: 2px; left: auto; }
.imcmsAdmin.ui-datepicker-rtl .ui-datepicker-next { left: 2px; right: auto; }
.imcmsAdmin.ui-datepicker-rtl .ui-datepicker-prev:hover { right: 1px; left: auto; }
.imcmsAdmin.ui-datepicker-rtl .ui-datepicker-next:hover { left: 1px; right: auto; }
.imcmsAdmin.ui-datepicker-rtl .ui-datepicker-buttonpane { clear:right; }
.imcmsAdmin.ui-datepicker-rtl .ui-datepicker-buttonpane button { float: left; }
.imcmsAdmin.ui-datepicker-rtl .ui-datepicker-buttonpane button.ui-datepicker-current { float:right; }
.imcmsAdmin.ui-datepicker-rtl .ui-datepicker-group { float:right; }
.imcmsAdmin.ui-datepicker-rtl .ui-datepicker-group-last .ui-datepicker-header { border-right-width:0; border-left-width:1px; }
.imcmsAdmin.ui-datepicker-rtl .ui-datepicker-group-middle .ui-datepicker-header { border-right-width:0; border-left-width:1px; }

/* IE6 IFRAME FIX (taken from datepicker 1.5.3 */
.imcmsAdmin.ui-datepicker-cover {
    display: none; /*sorry for IE5*/
    display/**/: block; /*sorry for IE5*/
    position: absolute; /*must have*/
    z-index: -1; /*must have*/
    filter: mask(); /*must have*/
    top: -4px; /*must have*/
    left: -4px; /*must have*/
    width: 200px; /*must have*/
    height: 200px; /*must have*/
}/*
 * jQuery UI Progressbar @VERSION
 *
 * Copyright 2010, AUTHORS.txt (http://jqueryui.com/about)
 * Dual licensed under the MIT or GPL Version 2 licenses.
 * http://jquery.org/license
 *
 * http://docs.jquery.com/UI/Progressbar#theming
 */
.imcmsAdmin.ui-progressbar { height:2em; text-align: left; }
.imcmsAdmin.ui-progressbar .ui-progressbar-value {margin: -1px; height:100%; }




/*
 * jQuery UI CSS Framework @VERSION
 *
 * Copyright 2010, AUTHORS.txt (http://jqueryui.com/about)
 * Dual licensed under the MIT or GPL Version 2 licenses.
 * http://jquery.org/license
 *
 * http://docs.jquery.com/UI/Theming/API
 */

/* Layout helpers
----------------------------------*/
.imcmsAdmin .ui-helper-hidden { display: none; }
.imcmsAdmin .ui-helper-hidden-accessible { position: absolute; left: -99999999px; }
.imcmsAdmin .ui-helper-reset { margin: 0; padding: 0; border: 0; outline: 0; line-height: 1.3; text-decoration: none; font-size: 100%; list-style: none; }
.imcmsAdmin .ui-helper-clearfix:after { content: "."; display: block; height: 0; clear: both; visibility: hidden; }
.imcmsAdmin .ui-helper-clearfix { display: inline-block; }
/* required comment for clearfix to work in Opera \*/
* html .ui-helper-clearfix { height:1%; }
.imcmsAdmin .ui-helper-clearfix { display:block; }
/* end clearfix */
.imcmsAdmin .ui-helper-zfix { width: 100%; height: 100%; top: 0; left: 0; position: absolute; opacity: 0; filter:Alpha(Opacity=0); }


/* Interaction Cues
----------------------------------*/
.imcmsAdmin .ui-state-disabled { cursor: default !important; }


/* Icons
----------------------------------*/

/* states and images */
.imcmsAdmin .ui-icon { display: block; text-indent: -99999px; overflow: hidden; background-repeat: no-repeat; }


/* Misc visuals
----------------------------------*/

/* Overlays */
.imcmsAdmin .ui-widget-overlay { position: absolute; top: 0; left: 0; width: 100%; height: 100%; }


/*
 * jQuery UI CSS Framework @VERSION
 *
 * Copyright 2010, AUTHORS.txt (http://jqueryui.com/about)
 * Dual licensed under the MIT or GPL Version 2 licenses.
 * http://jquery.org/license
 *
 * http://docs.jquery.com/UI/Theming/API
 *
 * To view and modify this theme, visit http://jqueryui.com/themeroller/?ffDefault=Lucida%20Grande,%20Lucida%20Sans,%20Arial,%20sans-serif&fwDefault=bold&fsDefault=1.1em&cornerRadius=5px&bgColorHeader=5c9ccc&bgTextureHeader=12_gloss_wave.png&bgImgOpacityHeader=55&borderColorHeader=4297d7&fcHeader=ffffff&iconColorHeader=d8e7f3&bgColorContent=fcfdfd&bgTextureContent=06_inset_hard.png&bgImgOpacityContent=100&borderColorContent=a6c9e2&fcContent=222222&iconColorContent=469bdd&bgColorDefault=dfeffc&bgTextureDefault=02_glass.png&bgImgOpacityDefault=85&borderColorDefault=c5dbec&fcDefault=2e6e9e&iconColorDefault=6da8d5&bgColorHover=d0e5f5&bgTextureHover=02_glass.png&bgImgOpacityHover=75&borderColorHover=79b7e7&fcHover=1d5987&iconColorHover=217bc0&bgColorActive=f5f8f9&bgTextureActive=06_inset_hard.png&bgImgOpacityActive=100&borderColorActive=79b7e7&fcActive=e17009&iconColorActive=f9bd01&bgColorHighlight=fbec88&bgTextureHighlight=01_flat.png&bgImgOpacityHighlight=55&borderColorHighlight=fad42e&fcHighlight=363636&iconColorHighlight=2e83ff&bgColorError=fef1ec&bgTextureError=02_glass.png&bgImgOpacityError=95&borderColorError=cd0a0a&fcError=cd0a0a&iconColorError=cd0a0a&bgColorOverlay=aaaaaa&bgTextureOverlay=01_flat.png&bgImgOpacityOverlay=0&opacityOverlay=30&bgColorShadow=aaaaaa&bgTextureShadow=01_flat.png&bgImgOpacityShadow=0&opacityShadow=30&thicknessShadow=8px&offsetTopShadow=-8px&offsetLeftShadow=-8px&cornerRadiusShadow=8px
 */


/* Component containers
----------------------------------*/
.imcmsAdmin .ui-widget { font-family: Lucida Grande, Lucida Sans, Arial, sans-serif; font-size: 1.1em; }
.imcmsAdmin .ui-widget .ui-widget { font-size: 1em; }
.imcmsAdmin .ui-widget input, .ui-widget select, .ui-widget textarea, .ui-widget button { font-family: Lucida Grande, Lucida Sans, Arial, sans-serif; font-size: 1em; }
.imcmsAdmin .ui-widget-content { border: 1px solid #a6c9e2; background: #fcfdfd url(images/ui-bg_inset-hard_100_fcfdfd_1x100.png) 50% bottom repeat-x; color: #222222; }
.imcmsAdmin .ui-widget-content a { color: #222222; }
.imcmsAdmin .ui-widget-header { border: 1px solid #4297d7; background: #5c9ccc url(images/ui-bg_gloss-wave_55_5c9ccc_500x100.png) 50% 50% repeat-x; color: #ffffff; font-weight: bold; }
.imcmsAdmin .ui-widget-header a { color: #ffffff; }

/* Interaction states
----------------------------------*/
.imcmsAdmin .ui-state-default, .ui-widget-content .ui-state-default, .ui-widget-header .ui-state-default { border: 1px solid #c5dbec; background: #dfeffc url(images/ui-bg_glass_85_dfeffc_1x400.png) 50% 50% repeat-x; font-weight: bold; color: #2e6e9e; }
.imcmsAdmin .ui-state-default a, .ui-state-default a:link, .ui-state-default a:visited { color: #2e6e9e; text-decoration: none; }
.imcmsAdmin .ui-state-hover, .ui-widget-content .ui-state-hover, .ui-widget-header .ui-state-hover, .ui-state-focus, .ui-widget-content .ui-state-focus, .ui-widget-header .ui-state-focus { border: 1px solid #79b7e7; background: #d0e5f5 url(images/ui-bg_glass_75_d0e5f5_1x400.png) 50% 50% repeat-x; font-weight: bold; color: #1d5987; }
.imcmsAdmin .ui-state-hover a, .ui-state-hover a:hover { color: #1d5987; text-decoration: none; }
.imcmsAdmin .ui-state-active, .ui-widget-content .ui-state-active, .ui-widget-header .ui-state-active { border: 1px solid #79b7e7; background: #f5f8f9 url(images/ui-bg_inset-hard_100_f5f8f9_1x100.png) 50% 50% repeat-x; font-weight: bold; color: #e17009; }
.imcmsAdmin .ui-state-active a, .ui-state-active a:link, .ui-state-active a:visited { color: #e17009; text-decoration: none; }
.imcmsAdmin .ui-widget :active { outline: none; }

/* Interaction Cues
----------------------------------*/
.imcmsAdmin .ui-state-highlight, .ui-widget-content .ui-state-highlight, .ui-widget-header .ui-state-highlight  {border: 1px solid #fad42e; background: #fbec88 url(images/ui-bg_flat_55_fbec88_40x100.png) 50% 50% repeat-x; color: #363636; }
.imcmsAdmin .ui-state-highlight a, .ui-widget-content .ui-state-highlight a,.ui-widget-header .ui-state-highlight a { color: #363636; }
.imcmsAdmin .ui-state-error, .ui-widget-content .ui-state-error, .ui-widget-header .ui-state-error {border: 1px solid #cd0a0a; background: #fef1ec url(images/ui-bg_glass_95_fef1ec_1x400.png) 50% 50% repeat-x; color: #cd0a0a; }
.imcmsAdmin .ui-state-error a, .ui-widget-content .ui-state-error a, .ui-widget-header .ui-state-error a { color: #cd0a0a; }
.imcmsAdmin .ui-state-error-text, .ui-widget-content .ui-state-error-text, .ui-widget-header .ui-state-error-text { color: #cd0a0a; }
.imcmsAdmin .ui-priority-primary, .ui-widget-content .ui-priority-primary, .ui-widget-header .ui-priority-primary { font-weight: bold; }
.imcmsAdmin .ui-priority-secondary, .ui-widget-content .ui-priority-secondary,  .ui-widget-header .ui-priority-secondary { opacity: .7; filter:Alpha(Opacity=70); font-weight: normal; }
.imcmsAdmin .ui-state-disabled, .ui-widget-content .ui-state-disabled, .ui-widget-header .ui-state-disabled { opacity: .35; filter:Alpha(Opacity=35); background-image: none; }

/* Icons
----------------------------------*/

/* states and images */
.imcmsAdmin .ui-icon { width: 16px; height: 16px; background-image: url(images/ui-icons_469bdd_256x240.png); }
.imcmsAdmin .ui-widget-content .ui-icon {background-image: url(images/ui-icons_469bdd_256x240.png); }
.imcmsAdmin .ui-widget-header .ui-icon {background-image: url(images/ui-icons_d8e7f3_256x240.png); }
.imcmsAdmin .ui-state-default .ui-icon { background-image: url(images/ui-icons_6da8d5_256x240.png); }
.imcmsAdmin .ui-state-hover .ui-icon, .ui-state-focus .ui-icon {background-image: url(images/ui-icons_217bc0_256x240.png); }
.imcmsAdmin .ui-state-active .ui-icon {background-image: url(images/ui-icons_f9bd01_256x240.png); }
.imcmsAdmin .ui-state-highlight .ui-icon {background-image: url(images/ui-icons_2e83ff_256x240.png); }
.imcmsAdmin .ui-state-error .ui-icon, .ui-state-error-text .ui-icon {background-image: url(images/ui-icons_cd0a0a_256x240.png); }

/* positioning */
.imcmsAdmin .ui-icon-carat-1-n { background-position: 0 0; }
.imcmsAdmin .ui-icon-carat-1-ne { background-position: -16px 0; }
.imcmsAdmin .ui-icon-carat-1-e { background-position: -32px 0; }
.imcmsAdmin .ui-icon-carat-1-se { background-position: -48px 0; }
.imcmsAdmin .ui-icon-carat-1-s { background-position: -64px 0; }
.imcmsAdmin .ui-icon-carat-1-sw { background-position: -80px 0; }
.imcmsAdmin .ui-icon-carat-1-w { background-position: -96px 0; }
.imcmsAdmin .ui-icon-carat-1-nw { background-position: -112px 0; }
.imcmsAdmin .ui-icon-carat-2-n-s { background-position: -128px 0; }
.imcmsAdmin .ui-icon-carat-2-e-w { background-position: -144px 0; }
.imcmsAdmin .ui-icon-triangle-1-n { background-position: 0 -16px; }
.imcmsAdmin .ui-icon-triangle-1-ne { background-position: -16px -16px; }
.imcmsAdmin .ui-icon-triangle-1-e { background-position: -32px -16px; }
.imcmsAdmin .ui-icon-triangle-1-se { background-position: -48px -16px; }
.imcmsAdmin .ui-icon-triangle-1-s { background-position: -64px -16px; }
.imcmsAdmin .ui-icon-triangle-1-sw { background-position: -80px -16px; }
.imcmsAdmin .ui-icon-triangle-1-w { background-position: -96px -16px; }
.imcmsAdmin .ui-icon-triangle-1-nw { background-position: -112px -16px; }
.imcmsAdmin .ui-icon-triangle-2-n-s { background-position: -128px -16px; }
.imcmsAdmin .ui-icon-triangle-2-e-w { background-position: -144px -16px; }
.imcmsAdmin .ui-icon-arrow-1-n { background-position: 0 -32px; }
.imcmsAdmin .ui-icon-arrow-1-ne { background-position: -16px -32px; }
.imcmsAdmin .ui-icon-arrow-1-e { background-position: -32px -32px; }
.imcmsAdmin .ui-icon-arrow-1-se { background-position: -48px -32px; }
.imcmsAdmin .ui-icon-arrow-1-s { background-position: -64px -32px; }
.imcmsAdmin .ui-icon-arrow-1-sw { background-position: -80px -32px; }
.imcmsAdmin .ui-icon-arrow-1-w { background-position: -96px -32px; }
.imcmsAdmin .ui-icon-arrow-1-nw { background-position: -112px -32px; }
.imcmsAdmin .ui-icon-arrow-2-n-s { background-position: -128px -32px; }
.imcmsAdmin .ui-icon-arrow-2-ne-sw { background-position: -144px -32px; }
.imcmsAdmin .ui-icon-arrow-2-e-w { background-position: -160px -32px; }
.imcmsAdmin .ui-icon-arrow-2-se-nw { background-position: -176px -32px; }
.imcmsAdmin .ui-icon-arrowstop-1-n { background-position: -192px -32px; }
.imcmsAdmin .ui-icon-arrowstop-1-e { background-position: -208px -32px; }
.imcmsAdmin .ui-icon-arrowstop-1-s { background-position: -224px -32px; }
.imcmsAdmin .ui-icon-arrowstop-1-w { background-position: -240px -32px; }
.imcmsAdmin .ui-icon-arrowthick-1-n { background-position: 0 -48px; }
.imcmsAdmin .ui-icon-arrowthick-1-ne { background-position: -16px -48px; }
.imcmsAdmin .ui-icon-arrowthick-1-e { background-position: -32px -48px; }
.imcmsAdmin .ui-icon-arrowthick-1-se { background-position: -48px -48px; }
.imcmsAdmin .ui-icon-arrowthick-1-s { background-position: -64px -48px; }
.imcmsAdmin .ui-icon-arrowthick-1-sw { background-position: -80px -48px; }
.imcmsAdmin .ui-icon-arrowthick-1-w { background-position: -96px -48px; }
.imcmsAdmin .ui-icon-arrowthick-1-nw { background-position: -112px -48px; }
.imcmsAdmin .ui-icon-arrowthick-2-n-s { background-position: -128px -48px; }
.imcmsAdmin .ui-icon-arrowthick-2-ne-sw { background-position: -144px -48px; }
.imcmsAdmin .ui-icon-arrowthick-2-e-w { background-position: -160px -48px; }
.imcmsAdmin .ui-icon-arrowthick-2-se-nw { background-position: -176px -48px; }
.imcmsAdmin .ui-icon-arrowthickstop-1-n { background-position: -192px -48px; }
.imcmsAdmin .ui-icon-arrowthickstop-1-e { background-position: -208px -48px; }
.imcmsAdmin .ui-icon-arrowthickstop-1-s { background-position: -224px -48px; }
.imcmsAdmin .ui-icon-arrowthickstop-1-w { background-position: -240px -48px; }
.imcmsAdmin .ui-icon-arrowreturnthick-1-w { background-position: 0 -64px; }
.imcmsAdmin .ui-icon-arrowreturnthick-1-n { background-position: -16px -64px; }
.imcmsAdmin .ui-icon-arrowreturnthick-1-e { background-position: -32px -64px; }
.imcmsAdmin .ui-icon-arrowreturnthick-1-s { background-position: -48px -64px; }
.imcmsAdmin .ui-icon-arrowreturn-1-w { background-position: -64px -64px; }
.imcmsAdmin .ui-icon-arrowreturn-1-n { background-position: -80px -64px; }
.imcmsAdmin .ui-icon-arrowreturn-1-e { background-position: -96px -64px; }
.imcmsAdmin .ui-icon-arrowreturn-1-s { background-position: -112px -64px; }
.imcmsAdmin .ui-icon-arrowrefresh-1-w { background-position: -128px -64px; }
.imcmsAdmin .ui-icon-arrowrefresh-1-n { background-position: -144px -64px; }
.imcmsAdmin .ui-icon-arrowrefresh-1-e { background-position: -160px -64px; }
.imcmsAdmin .ui-icon-arrowrefresh-1-s { background-position: -176px -64px; }
.imcmsAdmin .ui-icon-arrow-4 { background-position: 0 -80px; }
.imcmsAdmin .ui-icon-arrow-4-diag { background-position: -16px -80px; }
.imcmsAdmin .ui-icon-extlink { background-position: -32px -80px; }
.imcmsAdmin .ui-icon-newwin { background-position: -48px -80px; }
.imcmsAdmin .ui-icon-refresh { background-position: -64px -80px; }
.imcmsAdmin .ui-icon-shuffle { background-position: -80px -80px; }
.imcmsAdmin .ui-icon-transfer-e-w { background-position: -96px -80px; }
.imcmsAdmin .ui-icon-transferthick-e-w { background-position: -112px -80px; }
.imcmsAdmin .ui-icon-folder-collapsed { background-position: 0 -96px; }
.imcmsAdmin .ui-icon-folder-open { background-position: -16px -96px; }
.imcmsAdmin .ui-icon-document { background-position: -32px -96px; }
.imcmsAdmin .ui-icon-document-b { background-position: -48px -96px; }
.imcmsAdmin .ui-icon-note { background-position: -64px -96px; }
.imcmsAdmin .ui-icon-mail-closed { background-position: -80px -96px; }
.imcmsAdmin .ui-icon-mail-open { background-position: -96px -96px; }
.imcmsAdmin .ui-icon-suitcase { background-position: -112px -96px; }
.imcmsAdmin .ui-icon-comment { background-position: -128px -96px; }
.imcmsAdmin .ui-icon-person { background-position: -144px -96px; }
.imcmsAdmin .ui-icon-print { background-position: -160px -96px; }
.imcmsAdmin .ui-icon-trash { background-position: -176px -96px; }
.imcmsAdmin .ui-icon-locked { background-position: -192px -96px; }
.imcmsAdmin .ui-icon-unlocked { background-position: -208px -96px; }
.imcmsAdmin .ui-icon-bookmark { background-position: -224px -96px; }
.imcmsAdmin .ui-icon-tag { background-position: -240px -96px; }
.imcmsAdmin .ui-icon-home { background-position: 0 -112px; }
.imcmsAdmin .ui-icon-flag { background-position: -16px -112px; }
.imcmsAdmin .ui-icon-calendar { background-position: -32px -112px; }
.imcmsAdmin .ui-icon-cart { background-position: -48px -112px; }
.imcmsAdmin .ui-icon-pencil { background-position: -64px -112px; }
.imcmsAdmin .ui-icon-clock { background-position: -80px -112px; }
.imcmsAdmin .ui-icon-disk { background-position: -96px -112px; }
.imcmsAdmin .ui-icon-calculator { background-position: -112px -112px; }
.imcmsAdmin .ui-icon-zoomin { background-position: -128px -112px; }
.imcmsAdmin .ui-icon-zoomout { background-position: -144px -112px; }
.imcmsAdmin .ui-icon-search { background-position: -160px -112px; }
.imcmsAdmin .ui-icon-wrench { background-position: -176px -112px; }
.imcmsAdmin .ui-icon-gear { background-position: -192px -112px; }
.imcmsAdmin .ui-icon-heart { background-position: -208px -112px; }
.imcmsAdmin .ui-icon-star { background-position: -224px -112px; }
.imcmsAdmin .ui-icon-link { background-position: -240px -112px; }
.imcmsAdmin .ui-icon-cancel { background-position: 0 -128px; }
.imcmsAdmin .ui-icon-plus { background-position: -16px -128px; }
.imcmsAdmin .ui-icon-plusthick { background-position: -32px -128px; }
.imcmsAdmin .ui-icon-minus { background-position: -48px -128px; }
.imcmsAdmin .ui-icon-minusthick { background-position: -64px -128px; }
.imcmsAdmin .ui-icon-close { background-position: -80px -128px; }
.imcmsAdmin .ui-icon-closethick { background-position: -96px -128px; }
.imcmsAdmin .ui-icon-key { background-position: -112px -128px; }
.imcmsAdmin .ui-icon-lightbulb { background-position: -128px -128px; }
.imcmsAdmin .ui-icon-scissors { background-position: -144px -128px; }
.imcmsAdmin .ui-icon-clipboard { background-position: -160px -128px; }
.imcmsAdmin .ui-icon-copy { background-position: -176px -128px; }
.imcmsAdmin .ui-icon-contact { background-position: -192px -128px; }
.imcmsAdmin .ui-icon-image { background-position: -208px -128px; }
.imcmsAdmin .ui-icon-video { background-position: -224px -128px; }
.imcmsAdmin .ui-icon-script { background-position: -240px -128px; }
.imcmsAdmin .ui-icon-alert { background-position: 0 -144px; }
.imcmsAdmin .ui-icon-info { background-position: -16px -144px; }
.imcmsAdmin .ui-icon-notice { background-position: -32px -144px; }
.imcmsAdmin .ui-icon-help { background-position: -48px -144px; }
.imcmsAdmin .ui-icon-check { background-position: -64px -144px; }
.imcmsAdmin .ui-icon-bullet { background-position: -80px -144px; }
.imcmsAdmin .ui-icon-radio-off { background-position: -96px -144px; }
.imcmsAdmin .ui-icon-radio-on { background-position: -112px -144px; }
.imcmsAdmin .ui-icon-pin-w { background-position: -128px -144px; }
.imcmsAdmin .ui-icon-pin-s { background-position: -144px -144px; }
.imcmsAdmin .ui-icon-play { background-position: 0 -160px; }
.imcmsAdmin .ui-icon-pause { background-position: -16px -160px; }
.imcmsAdmin .ui-icon-seek-next { background-position: -32px -160px; }
.imcmsAdmin .ui-icon-seek-prev { background-position: -48px -160px; }
.imcmsAdmin .ui-icon-seek-end { background-position: -64px -160px; }
.imcmsAdmin .ui-icon-seek-start { background-position: -80px -160px; }
/* ui-icon-seek-first is deprecated, use ui-icon-seek-start instead */
.imcmsAdmin .ui-icon-seek-first { background-position: -80px -160px; }
.imcmsAdmin .ui-icon-stop { background-position: -96px -160px; }
.imcmsAdmin .ui-icon-eject { background-position: -112px -160px; }
.imcmsAdmin .ui-icon-volume-off { background-position: -128px -160px; }
.imcmsAdmin .ui-icon-volume-on { background-position: -144px -160px; }
.imcmsAdmin .ui-icon-power { background-position: 0 -176px; }
.imcmsAdmin .ui-icon-signal-diag { background-position: -16px -176px; }
.imcmsAdmin .ui-icon-signal { background-position: -32px -176px; }
.imcmsAdmin .ui-icon-battery-0 { background-position: -48px -176px; }
.imcmsAdmin .ui-icon-battery-1 { background-position: -64px -176px; }
.imcmsAdmin .ui-icon-battery-2 { background-position: -80px -176px; }
.imcmsAdmin .ui-icon-battery-3 { background-position: -96px -176px; }
.imcmsAdmin .ui-icon-circle-plus { background-position: 0 -192px; }
.imcmsAdmin .ui-icon-circle-minus { background-position: -16px -192px; }
.imcmsAdmin .ui-icon-circle-close { background-position: -32px -192px; }
.imcmsAdmin .ui-icon-circle-triangle-e { background-position: -48px -192px; }
.imcmsAdmin .ui-icon-circle-triangle-s { background-position: -64px -192px; }
.imcmsAdmin .ui-icon-circle-triangle-w { background-position: -80px -192px; }
.imcmsAdmin .ui-icon-circle-triangle-n { background-position: -96px -192px; }
.imcmsAdmin .ui-icon-circle-arrow-e { background-position: -112px -192px; }
.imcmsAdmin .ui-icon-circle-arrow-s { background-position: -128px -192px; }
.imcmsAdmin .ui-icon-circle-arrow-w { background-position: -144px -192px; }
.imcmsAdmin .ui-icon-circle-arrow-n { background-position: -160px -192px; }
.imcmsAdmin .ui-icon-circle-zoomin { background-position: -176px -192px; }
.imcmsAdmin .ui-icon-circle-zoomout { background-position: -192px -192px; }
.imcmsAdmin .ui-icon-circle-check { background-position: -208px -192px; }
.imcmsAdmin .ui-icon-circlesmall-plus { background-position: 0 -208px; }
.imcmsAdmin .ui-icon-circlesmall-minus { background-position: -16px -208px; }
.imcmsAdmin .ui-icon-circlesmall-close { background-position: -32px -208px; }
.imcmsAdmin .ui-icon-squaresmall-plus { background-position: -48px -208px; }
.imcmsAdmin .ui-icon-squaresmall-minus { background-position: -64px -208px; }
.imcmsAdmin .ui-icon-squaresmall-close { background-position: -80px -208px; }
.imcmsAdmin .ui-icon-grip-dotted-vertical { background-position: 0 -224px; }
.imcmsAdmin .ui-icon-grip-dotted-horizontal { background-position: -16px -224px; }
.imcmsAdmin .ui-icon-grip-solid-vertical { background-position: -32px -224px; }
.imcmsAdmin .ui-icon-grip-solid-horizontal { background-position: -48px -224px; }
.imcmsAdmin .ui-icon-gripsmall-diagonal-se { background-position: -64px -224px; }
.imcmsAdmin .ui-icon-grip-diagonal-se { background-position: -80px -224px; }


/* Misc visuals
----------------------------------*/

/* Corner radius */
.imcmsAdmin .ui-corner-tl { -moz-border-radius-topleft: 5px; -webkit-border-top-left-radius: 5px; border-top-left-radius: 5px; }
.imcmsAdmin .ui-corner-tr { -moz-border-radius-topright: 5px; -webkit-border-top-right-radius: 5px; border-top-right-radius: 5px; }
.imcmsAdmin .ui-corner-bl { -moz-border-radius-bottomleft: 5px; -webkit-border-bottom-left-radius: 5px; border-bottom-left-radius: 5px; }
.imcmsAdmin .ui-corner-br { -moz-border-radius-bottomright: 5px; -webkit-border-bottom-right-radius: 5px; border-bottom-right-radius: 5px; }
.imcmsAdmin .ui-corner-top { -moz-border-radius-topleft: 5px; -webkit-border-top-left-radius: 5px; border-top-left-radius: 5px; -moz-border-radius-topright: 5px; -webkit-border-top-right-radius: 5px; border-top-right-radius: 5px; }
.imcmsAdmin .ui-corner-bottom { -moz-border-radius-bottomleft: 5px; -webkit-border-bottom-left-radius: 5px; border-bottom-left-radius: 5px; -moz-border-radius-bottomright: 5px; -webkit-border-bottom-right-radius: 5px; border-bottom-right-radius: 5px; }
.imcmsAdmin .ui-corner-right {  -moz-border-radius-topright: 5px; -webkit-border-top-right-radius: 5px; border-top-right-radius: 5px; -moz-border-radius-bottomright: 5px; -webkit-border-bottom-right-radius: 5px; border-bottom-right-radius: 5px; }
.imcmsAdmin .ui-corner-left { -moz-border-radius-topleft: 5px; -webkit-border-top-left-radius: 5px; border-top-left-radius: 5px; -moz-border-radius-bottomleft: 5px; -webkit-border-bottom-left-radius: 5px; border-bottom-left-radius: 5px; }
.imcmsAdmin .ui-corner-all { -moz-border-radius: 5px; -webkit-border-radius: 5px; border-radius: 5px; }

/* Overlays */
.imcmsAdmin .ui-widget-overlay { background: #aaaaaa url(images/ui-bg_flat_0_aaaaaa_40x100.png) 50% 50% repeat-x; opacity: .30;filter:Alpha(Opacity=30); }
.imcmsAdmin .ui-widget-shadow { margin: -8px 0 0 -8px; padding: 8px; background: #aaaaaa url(images/ui-bg_flat_0_aaaaaa_40x100.png) 50% 50% repeat-x; opacity: .30;filter:Alpha(Opacity=30); -moz-border-radius: 8px; -webkit-border-radius: 8px; border-radius: 8px; }/*
 * jQuery UI Resizable @VERSION
 *
 * Copyright 2010, AUTHORS.txt (http://jqueryui.com/about)
 * Dual licensed under the MIT or GPL Version 2 licenses.
 * http://jquery.org/license
 *
 * http://docs.jquery.com/UI/Resizable#theming
 */
.imcmsAdmin .ui-resizable { position: relative;}
.imcmsAdmin .ui-resizable-handle { position: absolute;font-size: 0.1px;z-index: 99999; display: block;}
.imcmsAdmin .ui-resizable-disabled .ui-resizable-handle, .ui-resizable-autohide .ui-resizable-handle { display: none; }
.imcmsAdmin .ui-resizable-n { cursor: n-resize; height: 7px; width: 100%; top: -5px; left: 0; }
.imcmsAdmin .ui-resizable-s { cursor: s-resize; height: 7px; width: 100%; bottom: -5px; left: 0; }
.imcmsAdmin .ui-resizable-e { cursor: e-resize; width: 7px; right: -5px; top: 0; height: 100%; }
.imcmsAdmin .ui-resizable-w { cursor: w-resize; width: 7px; left: -5px; top: 0; height: 100%; }
.imcmsAdmin .ui-resizable-se { cursor: se-resize; width: 12px; height: 12px; right: 1px; bottom: 1px; }
.imcmsAdmin .ui-resizable-sw { cursor: sw-resize; width: 9px; height: 9px; left: -5px; bottom: -5px; }
.imcmsAdmin .ui-resizable-nw { cursor: nw-resize; width: 9px; height: 9px; left: -5px; top: -5px; }
.imcmsAdmin .ui-resizable-ne { cursor: ne-resize; width: 9px; height: 9px; right: -5px; top: -5px;}/*
 * jQuery UI Selectable @VERSION
 *
 * Copyright 2010, AUTHORS.txt (http://jqueryui.com/about)
 * Dual licensed under the MIT or GPL Version 2 licenses.
 * http://jquery.org/license
 *
 * http://docs.jquery.com/UI/Selectable#theming
 */
.imcmsAdmin .ui-selectable-helper { position: absolute; z-index: 100; border:1px dotted black; }
/*
 * jQuery UI Accordion @VERSION
 *
 * Copyright 2010, AUTHORS.txt (http://jqueryui.com/about)
 * Dual licensed under the MIT or GPL Version 2 licenses.
 * http://jquery.org/license
 *
 * http://docs.jquery.com/UI/Accordion#theming
 */
/* IE/Win - Fix animation bug - #4615 */
.imcmsAdmin .ui-accordion { width: 100%; }
.imcmsAdmin .ui-accordion .ui-accordion-header { cursor: pointer; position: relative; margin-top: 1px; zoom: 1; }
.imcmsAdmin .ui-accordion .ui-accordion-li-fix { display: inline; }
.imcmsAdmin .ui-accordion .ui-accordion-header-active { border-bottom: 0 !important; }
.imcmsAdmin .ui-accordion .ui-accordion-header a { display: block; font-size: 1em; padding: .5em .5em .5em .7em; }
.imcmsAdmin .ui-accordion-icons .ui-accordion-header a { padding-left: 2.2em; }
.imcmsAdmin .ui-accordion .ui-accordion-header .ui-icon { position: absolute; left: .5em; top: 50%; margin-top: -8px; }
.imcmsAdmin .ui-accordion .ui-accordion-content { padding: 1em 2.2em; border-top: 0; margin-top: -2px; position: relative; top: 1px; margin-bottom: 2px; overflow: auto; display: none; zoom: 1; }
.imcmsAdmin .ui-accordion .ui-accordion-content-active { display: block; }/*
 * jQuery UI Autocomplete @VERSION
 *
 * Copyright 2010, AUTHORS.txt (http://jqueryui.com/about)
 * Dual licensed under the MIT or GPL Version 2 licenses.
 * http://jquery.org/license
 *
 * http://docs.jquery.com/UI/Autocomplete#theming
 */
.imcmsAdmin .ui-autocomplete { position: absolute; cursor: default; }	

/* workarounds */
* html .ui-autocomplete { width:1px; } /* without this, the menu expands to 100% in IE6 */

/*
 * jQuery UI Menu @VERSION
 *
 * Copyright 2010, AUTHORS.txt (http://jqueryui.com/about)
 * Dual licensed under the MIT or GPL Version 2 licenses.
 * http://jquery.org/license
 *
 * http://docs.jquery.com/UI/Menu#theming
 */
.imcmsAdmin .ui-menu {
	list-style:none;
	padding: 2px;
	margin: 0;
	display:block;
	float: left;
}
.imcmsAdmin .ui-menu .ui-menu {
	margin-top: -3px;
}
.imcmsAdmin .ui-menu .ui-menu-item {
	margin:0;
	padding: 0;
	zoom: 1;
	float: left;
	clear: left;
	width: 100%;
}
.imcmsAdmin .ui-menu .ui-menu-item a {
	text-decoration:none;
	display:block;
	padding:.2em .4em;
	line-height:1.5;
	zoom:1;
}
.imcmsAdmin .ui-menu .ui-menu-item a.ui-state-hover,
.imcmsAdmin .ui-menu .ui-menu-item a.ui-state-active {
	font-weight: normal;
	margin: -1px;
}
/*
 * jQuery UI Button @VERSION
 *
 * Copyright 2010, AUTHORS.txt (http://jqueryui.com/about)
 * Dual licensed under the MIT or GPL Version 2 licenses.
 * http://jquery.org/license
 *
 * http://docs.jquery.com/UI/Button#theming
 */
.imcmsAdmin .ui-button { display: inline-block; position: relative; padding: 0; margin-right: .1em; text-decoration: none !important; cursor: pointer; text-align: center; zoom: 1; overflow: visible; } /* the overflow property removes extra width in IE */
.imcmsAdmin .ui-button-icon-only { width: 2.2em; } /* to make room for the icon, a width needs to be set here */
button.ui-button-icon-only { width: 2.4em; } /* button elements seem to need a little more width */
.imcmsAdmin .ui-button-icons-only { width: 3.4em; } 
button.ui-button-icons-only { width: 3.7em; } 

/*button text element */
.imcmsAdmin .ui-button .ui-button-text { display: block; line-height: 1.4;  }
.imcmsAdmin .ui-button-text-only .ui-button-text { padding: .4em 1em; }
.imcmsAdmin .ui-button-icon-only .ui-button-text, .ui-button-icons-only .ui-button-text { padding: .4em; text-indent: -9999999px; }
.imcmsAdmin .ui-button-text-icon-primary .ui-button-text, .ui-button-text-icons .ui-button-text { padding: .4em 1em .4em 2.1em; }
.imcmsAdmin .ui-button-text-icon-secondary .ui-button-text, .ui-button-text-icons .ui-button-text { padding: .4em 2.1em .4em 1em; }
.imcmsAdmin .ui-button-text-icons .ui-button-text { padding-left: 2.1em; padding-right: 2.1em; }
/* no icon support for input elements, provide padding by default */
input.ui-button { padding: .4em 1em; }

/*button icon element(s) */
.imcmsAdmin .ui-button-icon-only .ui-icon, .ui-button-text-icon-primary .ui-icon, .ui-button-text-icon-secondary .ui-icon, .ui-button-text-icons .ui-icon, .ui-button-icons-only .ui-icon { position: absolute; top: 50%; margin-top: -8px; }
.imcmsAdmin .ui-button-icon-only .ui-icon { left: 50%; margin-left: -8px; }
.imcmsAdmin .ui-button-text-icon-primary .ui-button-icon-primary, .ui-button-text-icons .ui-button-icon-primary, .ui-button-icons-only .ui-button-icon-primary { left: .5em; }
.imcmsAdmin .ui-button-text-icon-secondary .ui-button-icon-secondary, .ui-button-text-icons .ui-button-icon-secondary, .ui-button-icons-only .ui-button-icon-secondary { right: .5em; }
.imcmsAdmin .ui-button-text-icons .ui-button-icon-secondary, .ui-button-icons-only .ui-button-icon-secondary { right: .5em; }

/*button sets*/
.imcmsAdmin .ui-buttonset { margin-right: 7px; }
.imcmsAdmin .ui-buttonset .ui-button { margin-left: 0; margin-right: -.3em; }

/* workarounds */
button.ui-button::-moz-focus-inner { border: 0; padding: 0; } /* reset extra padding in Firefox */
/*
 * jQuery UI Dialog @VERSION
 *
 * Copyright 2010, AUTHORS.txt (http://jqueryui.com/about)
 * Dual licensed under the MIT or GPL Version 2 licenses.
 * http://jquery.org/license
 *
 * http://docs.jquery.com/UI/Dialog#theming
 */
.imcmsAdmin .ui-dialog { position: absolute; padding: .2em; width: 300px; overflow: hidden; }
.imcmsAdmin .ui-dialog .ui-dialog-titlebar { padding: .5em 1em .3em; position: relative;  }
.imcmsAdmin .ui-dialog .ui-dialog-title { float: left; margin: .1em 16px .2em 0; } 
.imcmsAdmin .ui-dialog .ui-dialog-titlebar-close { position: absolute; right: .3em; top: 50%; width: 19px; margin: -10px 0 0 0; padding: 1px; height: 18px; }
.imcmsAdmin .ui-dialog .ui-dialog-titlebar-close span { display: block; margin: 1px; }
.imcmsAdmin .ui-dialog .ui-dialog-titlebar-close:hover, .ui-dialog .ui-dialog-titlebar-close:focus { padding: 0; }
.imcmsAdmin .ui-dialog .ui-dialog-content { position: relative; border: 0; padding: .5em 1em; background: none; overflow: auto; zoom: 1; }
.imcmsAdmin .ui-dialog .ui-dialog-buttonpane { text-align: left; border-width: 1px 0 0 0; background-image: none; margin: .5em 0 0 0; padding: .3em 1em .5em .4em; }
.imcmsAdmin .ui-dialog .ui-dialog-buttonpane .ui-dialog-buttonset { float: right; }
.imcmsAdmin .ui-dialog .ui-dialog-buttonpane button { margin: .5em .4em .5em 0; cursor: pointer; }
.imcmsAdmin .ui-dialog .ui-resizable-se { width: 14px; height: 14px; right: 3px; bottom: 3px; }
.imcmsAdmin .ui-draggable .ui-dialog-titlebar { cursor: move; }
/*
 * jQuery UI Slider @VERSION
 *
 * Copyright 2010, AUTHORS.txt (http://jqueryui.com/about)
 * Dual licensed under the MIT or GPL Version 2 licenses.
 * http://jquery.org/license
 *
 * http://docs.jquery.com/UI/Slider#theming
 */
.imcmsAdmin .ui-slider { position: relative; text-align: left; }
.imcmsAdmin .ui-slider .ui-slider-handle { position: absolute; z-index: 2; width: 1.2em; height: 1.2em; cursor: default; }
.imcmsAdmin .ui-slider .ui-slider-range { position: absolute; z-index: 1; font-size: .7em; display: block; border: 0; background-position: 0 0; }

.imcmsAdmin .ui-slider-horizontal { height: .8em; }
.imcmsAdmin .ui-slider-horizontal .ui-slider-handle { top: -.3em; margin-left: -.6em; }
.imcmsAdmin .ui-slider-horizontal .ui-slider-range { top: 0; height: 100%; }
.imcmsAdmin .ui-slider-horizontal .ui-slider-range-min { left: 0; }
.imcmsAdmin .ui-slider-horizontal .ui-slider-range-max { right: 0; }

.imcmsAdmin .ui-slider-vertical { width: .8em; height: 100px; }
.imcmsAdmin .ui-slider-vertical .ui-slider-handle { left: -.3em; margin-left: 0; margin-bottom: -.6em; }
.imcmsAdmin .ui-slider-vertical .ui-slider-range { left: 0; width: 100%; }
.imcmsAdmin .ui-slider-vertical .ui-slider-range-min { bottom: 0; }
.imcmsAdmin .ui-slider-vertical .ui-slider-range-max { top: 0; }/*
 * jQuery UI Tabs @VERSION
 *
 * Copyright 2010, AUTHORS.txt (http://jqueryui.com/about)
 * Dual licensed under the MIT or GPL Version 2 licenses.
 * http://jquery.org/license
 *
 * http://docs.jquery.com/UI/Tabs#theming
 */
.imcmsAdmin .ui-tabs { position: relative; padding: .2em; zoom: 1; } /* position: relative prevents IE scroll bug (element with position: relative inside container with overflow: auto appear as "fixed") */
.imcmsAdmin .ui-tabs .ui-tabs-nav { margin: 0; padding: .2em .2em 0; }
.imcmsAdmin .ui-tabs .ui-tabs-nav li { list-style: none; float: left; position: relative; top: 1px; margin: 0 .2em 1px 0; border-bottom: 0 !important; padding: 0; white-space: nowrap; }
.imcmsAdmin .ui-tabs .ui-tabs-nav li a { float: left; padding: .5em 1em; text-decoration: none; }
.imcmsAdmin .ui-tabs .ui-tabs-nav li.ui-tabs-selected { margin-bottom: 0; padding-bottom: 1px; }
.imcmsAdmin .ui-tabs .ui-tabs-nav li.ui-tabs-selected a, .ui-tabs .ui-tabs-nav li.ui-state-disabled a, .ui-tabs .ui-tabs-nav li.ui-state-processing a { cursor: text; }
.imcmsAdmin .ui-tabs .ui-tabs-nav li a, .ui-tabs.ui-tabs-collapsible .ui-tabs-nav li.ui-tabs-selected a { cursor: pointer; } /* first selector in group seems obsolete, but required to overcome bug in Opera applying cursor: text overall if defined elsewhere... */
.imcmsAdmin .ui-tabs .ui-tabs-panel { display: block; border-width: 0; padding: 1em 1.4em; background: none; }
.imcmsAdmin .ui-tabs .ui-tabs-hide { display: none !important; }
/*
 * jQuery UI Datepicker @VERSION
 *
 * Copyright 2010, AUTHORS.txt (http://jqueryui.com/about)
 * Dual licensed under the MIT or GPL Version 2 licenses.
 * http://jquery.org/license
 *
 * http://docs.jquery.com/UI/Datepicker#theming
 */
.imcmsAdmin .ui-datepicker { width: 17em; padding: .2em .2em 0; }
.imcmsAdmin .ui-datepicker .ui-datepicker-header { position:relative; padding:.2em 0; }
.imcmsAdmin .ui-datepicker .ui-datepicker-prev, .ui-datepicker .ui-datepicker-next { position:absolute; top: 2px; width: 1.8em; height: 1.8em; }
.imcmsAdmin .ui-datepicker .ui-datepicker-prev-hover, .ui-datepicker .ui-datepicker-next-hover { top: 1px; }
.imcmsAdmin .ui-datepicker .ui-datepicker-prev { left:2px; }
.imcmsAdmin .ui-datepicker .ui-datepicker-next { right:2px; }
.imcmsAdmin .ui-datepicker .ui-datepicker-prev-hover { left:1px; }
.imcmsAdmin .ui-datepicker .ui-datepicker-next-hover { right:1px; }
.imcmsAdmin .ui-datepicker .ui-datepicker-prev span, .ui-datepicker .ui-datepicker-next span { display: block; position: absolute; left: 50%; margin-left: -8px; top: 50%; margin-top: -8px;  }
.imcmsAdmin .ui-datepicker .ui-datepicker-title { margin: 0 2.3em; line-height: 1.8em; text-align: center; }
.imcmsAdmin .ui-datepicker .ui-datepicker-title select { font-size:1em; margin:1px 0; }
.imcmsAdmin .ui-datepicker select.ui-datepicker-month-year {width: 100%;}
.imcmsAdmin .ui-datepicker select.ui-datepicker-month, 
.imcmsAdmin .ui-datepicker select.ui-datepicker-year { width: 49%;}
.imcmsAdmin .ui-datepicker table {width: 100%; font-size: .9em; border-collapse: collapse; margin:0 0 .4em; }
.imcmsAdmin .ui-datepicker th { padding: .7em .3em; text-align: center; font-weight: bold; border: 0;  }
.imcmsAdmin .ui-datepicker td { border: 0; padding: 1px; }
.imcmsAdmin .ui-datepicker td span, .ui-datepicker td a { display: block; padding: .2em; text-align: right; text-decoration: none; }
.imcmsAdmin .ui-datepicker .ui-datepicker-buttonpane { background-image: none; margin: .7em 0 0 0; padding:0 .2em; border-left: 0; border-right: 0; border-bottom: 0; }
.imcmsAdmin .ui-datepicker .ui-datepicker-buttonpane button { float: right; margin: .5em .2em .4em; cursor: pointer; padding: .2em .6em .3em .6em; width:auto; overflow:visible; }
.imcmsAdmin .ui-datepicker .ui-datepicker-buttonpane button.ui-datepicker-current { float:left; }

/* with multiple calendars */
.imcmsAdmin .ui-datepicker.ui-datepicker-multi { width:auto; }
.imcmsAdmin .ui-datepicker-multi .ui-datepicker-group { float:left; }
.imcmsAdmin .ui-datepicker-multi .ui-datepicker-group table { width:95%; margin:0 auto .4em; }
.imcmsAdmin .ui-datepicker-multi-2 .ui-datepicker-group { width:50%; }
.imcmsAdmin .ui-datepicker-multi-3 .ui-datepicker-group { width:33.3%; }
.imcmsAdmin .ui-datepicker-multi-4 .ui-datepicker-group { width:25%; }
.imcmsAdmin .ui-datepicker-multi .ui-datepicker-group-last .ui-datepicker-header { border-left-width:0; }
.imcmsAdmin .ui-datepicker-multi .ui-datepicker-group-middle .ui-datepicker-header { border-left-width:0; }
.imcmsAdmin .ui-datepicker-multi .ui-datepicker-buttonpane { clear:left; }
.imcmsAdmin .ui-datepicker-row-break { clear:both; width:100%; }

/* RTL support */
.imcmsAdmin .ui-datepicker-rtl { direction: rtl; }
.imcmsAdmin .ui-datepicker-rtl .ui-datepicker-prev { right: 2px; left: auto; }
.imcmsAdmin .ui-datepicker-rtl .ui-datepicker-next { left: 2px; right: auto; }
.imcmsAdmin .ui-datepicker-rtl .ui-datepicker-prev:hover { right: 1px; left: auto; }
.imcmsAdmin .ui-datepicker-rtl .ui-datepicker-next:hover { left: 1px; right: auto; }
.imcmsAdmin .ui-datepicker-rtl .ui-datepicker-buttonpane { clear:right; }
.imcmsAdmin .ui-datepicker-rtl .ui-datepicker-buttonpane button { float: left; }
.imcmsAdmin .ui-datepicker-rtl .ui-datepicker-buttonpane button.ui-datepicker-current { float:right; }
.imcmsAdmin .ui-datepicker-rtl .ui-datepicker-group { float:right; }
.imcmsAdmin .ui-datepicker-rtl .ui-datepicker-group-last .ui-datepicker-header { border-right-width:0; border-left-width:1px; }
.imcmsAdmin .ui-datepicker-rtl .ui-datepicker-group-middle .ui-datepicker-header { border-right-width:0; border-left-width:1px; }

/* IE6 IFRAME FIX (taken from datepicker 1.5.3 */
.imcmsAdmin .ui-datepicker-cover {
    display: none; /*sorry for IE5*/
    display/**/: block; /*sorry for IE5*/
    position: absolute; /*must have*/
    z-index: -1; /*must have*/
    filter: mask(); /*must have*/
    top: -4px; /*must have*/
    left: -4px; /*must have*/
    width: 200px; /*must have*/
    height: 200px; /*must have*/
}/*
 * jQuery UI Progressbar @VERSION
 *
 * Copyright 2010, AUTHORS.txt (http://jqueryui.com/about)
 * Dual licensed under the MIT or GPL Version 2 licenses.
 * http://jquery.org/license
 *
 * http://docs.jquery.com/UI/Progressbar#theming
 */
.imcmsAdmin .ui-progressbar { height:2em; text-align: left; }
.imcmsAdmin .ui-progressbar .ui-progressbar-value {margin: -1px; height:100%; }


