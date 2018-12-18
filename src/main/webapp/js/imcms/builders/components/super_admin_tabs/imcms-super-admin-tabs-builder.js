/**
 * @author Serhii Maksymchuk from Ubrainians for imCode
 * 22.06.18
 */

const WindowTabsBuilder = require('imcms-window-tabs-builder');

module.exports = new WindowTabsBuilder({
    tabBuilders: [
        require('imcms-users-tab-builder'),
        require('imcms-roles-tab-builder'),
        require('imcms-ip-access-tab-builder'),
        require('imcms-ip-white-list-tab-builder'),
        require('imcms-delete-docs-tab-builder'),
        require('imcms-templates-admin-tab-builder'),
        require('imcms-files-tab-builder'),
        require('imcms-search-tab-builder'),
        require('imcms-link-validator-tab-builder'),
        require('imcms-categories-admin-tab-builder'),
        require('imcms-profiles-tab-builder'),
        require('imcms-system-properties-tab-builder'),
    ]
});