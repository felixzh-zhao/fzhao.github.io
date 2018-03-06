import { Injectable } from "@angular/core";
import { BehaviorSubject, Observable, Subject, Subscription } from "rxjs/Rx";
import { isPresent } from "../utilities/utilities-export";
import { NavHeader, NavItem } from "../interfaces/nav-item";
import { configfqnames, Logger } from "./services-export";
import { FeatureService } from './feature.service';
import * as navConfig from "./navigation.config";
import * as _ from "lodash";

@Injectable()
export class NavigationService {
    private readonly MOBILE_MAX_WIDTH: number = 768;
    private _navSource: Subject<any>;
    private _navItem: NavItem;
    private _navSideBar: boolean;
    private _isMobileMode: boolean = false;
    private _navigationMenu: NavHeader[];
    private _navMenuSubject: BehaviorSubject<NavHeader[]>;
    private _uID: string;

    constructor(private _logger: Logger, private _ftrSvc: FeatureService) {
        this._uID = this._logger.generateUID();
        this._navSource = new Subject();
        this._navMenuSubject = new BehaviorSubject<NavHeader[]>(null);
        this._navigationMenu = navConfig.menu;
        this.doNavMenuInit();
        this.doFeatureSubscriptions();
    }

    /**
     * @name getUID
     *
     * @description
     * Returns the unique ID for this instance
     *
     * @returns {string}
     */
    getUID(): string {
        return this._uID;
    }

    /**
     * @name updateIsMobileMode
     *
     * @description
     * Checks if we need to switch to mobile mode based on the current width
     *
     * @param {number} width The width of the App to determine if it is a mobile resolution
     */
    updateIsMobileMode(width: number) {
        let isMobileRes = width <= this.MOBILE_MAX_WIDTH;
        let wasMobile = this._isMobileMode;
        this._isMobileMode = isMobileRes;

        // When first starting mobile mode, close the nav bar
        if (isMobileRes && !wasMobile) {
            this.setSideBarVisibility(!isMobileRes);
        }
    }

    /**
     * @name getIsMobileMode
     *
     * @description
     * Get if the UI is currently in mobile mode (mobile resolution or lower)
     *
     * @return {boolean} isMobile
     */
    getIsMobileMode(): boolean {
        return this._isMobileMode;
    }

    /**
     * @name toggleSideBar
     *
     * @description
     * Toggles the collapsed state of the sidebar on the MainApp
     *
     */
    toggleSideBar(): void {
        this._navSideBar = !this._navSideBar;
        this.updateSource();
    }

    /**
     * @name setSideBarVisibility
     *
     * @description
     * Hide or show the sidebar, true means visible
     *
     * @param visible
     */
    setSideBarVisibility(visible: boolean): void {
        this._navSideBar = !!visible;
        this.updateSource();
    }

    /**
     * @name changeNavigation
     *
     * @description
     * Changes the currently selected navigation item to the one specified
     *
     * @param navItem
     */
    changeNavigation(navItem: NavItem): void {
        if (isPresent(navItem)) {
            this._navItem = navItem;
            this.updateSource();
        }
    }

    /**
     * @name subscribe
     *
     * @description
     * Returns a subscription to the navigation source
     *
     * @param observer
     * @return {Subscription}
     */
    subscribe(observer: any): Subscription {
        return this._navSource.subscribe(observer);
    }

    /**
     * @name setNavItemVisibility
     *
     * @description
     * Sets a particular navigation item or header to be visible (true) or invisible (false)
     *
     * @param unique
     * @param visibility
     */
    setNavItemVisibility(unique: string | string[], visibility: boolean): void {
        visibility = !!visibility;
        let ids: string[] = [];
        if (unique.constructor !== Array) {
            ids = [unique] as string[];
        } else {
            ids = unique as string[];
        }

        for (let id of ids) {
            let nothingFound = true;
            let found: NavHeader = _.find(this._navigationMenu, ['uniq', id]);
            if (found) {
                nothingFound = false;
                this._logger.debug(`[NavigationService] setVisibility(): Changing ${id} to visibility: ${visibility}`);
                found.hidden = !visibility;
                this.updateNavMenu();
            } else {
                for (let header of this._navigationMenu) {
                    let childFound: NavItem = _.find(header.navItems, ['uniq', id]);
                    if (childFound) {
                        nothingFound = false;
                        this._logger.debug(`[NavigationService] setVisibility(): Changing ${id} to visibility: ${visibility}`);
                        childFound.hidden = !visibility;
                        this.updateNavMenu();
                    }
                }
            }

            if (nothingFound) this._logger.error(`[NavigationService] setVisibility(): Could not find any navigation item with unique name ${unique}`);
        }
    }

    /**
     * @name getNavigationMenu
     *
     * @description
     * Returns an anonymous observable cast from the navigation menu Subject of the service
     */
    getNavigationMenu(): Observable<NavHeader[]> {
        return this._navMenuSubject.asObservable().share();
    }

    private doNavMenuInit(): void {
        this._navMenuSubject.next(this._navigationMenu);
        this._navMenuSubject.share();
    }

    private updateSource(): void {
        this._navSource.next({
            sidebar: this._navSideBar,
            navItem: this._navItem
        });
    }

    private doFeatureSubscriptions(): void {
        const callFeatureItems = [
            'h323',
            'sip',
            'network-quality',
            'place-a-call',
            'dir-server',
            'call-settings',
            'cal-server',
            'dialing-preference',
            'call-configuration',
            'recent-calls',
            'global-security',
            'time-in-call',
            'servers',
            'snmp'
        ];

        const panoFeatureItems = [
            'provisioning-server'
        ];

        // Call Service Feature
        this._ftrSvc.isFeatureEnabled$(configfqnames.FEATURE_MASTER_CALLSERVICE_ENABLED).subscribe({
            next: (enabled: boolean) => {
                this._logger.debug(`[NavigationService] Call Service feature is enabled: ${enabled}`);
                this.setNavItemVisibility(callFeatureItems, enabled);
            },

            error: (err) =>
                this._logger.error('[NavigationService] Could not subscribe for feature configs! ', err)

        });

        // Feature or page only for pano
        this._ftrSvc.isFeatureEnabled$(configfqnames.FEATURE_MASTER_CALLSERVICE_ENABLED).subscribe({
            next: (enabled: boolean) => {
                this._logger.debug(`[NavigationService] Call Service feature is not enabled: ${enabled}`);
                this.setNavItemVisibility(panoFeatureItems, !enabled);
            },

            error: (err) =>
                this._logger.error('[NavigationService] Could not subscribe for feature configs! ', err)

        });

        // Developer Feature
        this._ftrSvc.isFeatureEnabled$(configfqnames.SYSTEM_INFO_DEVFEATURES).subscribe({
            next: (enabled: boolean) => {
                this._logger.debug(`[NavigationService] Devfeatures is enabled: ${enabled}`);
                this.setNavItemVisibility(['sandbox'], enabled);
            },

            error: (err) =>
                this._logger.error('[NavigationService] Could not subscribe for feature configs! ', err)

        });

        // Wi-Fi Options Feature
        this._ftrSvc.isFeatureEnabled$(configfqnames.SYSTEM_NETWORK_WIRELESS_WIFI_NETWORKACCESS_ENABLED).subscribe({
            next: (enabled: boolean) => {
                this._logger.debug(`[NavigationService] Wi-Fi nav link is enabled: ${enabled}`);
                this.setNavItemVisibility(['wifi-options'], enabled);
            },

            error: (err) => {
                this._logger.error('[NavigationService] Could not subscribe for feature configs: ', err);
            }
        });
    }

    private updateNavMenu(): void {
        this._navMenuSubject.next(this._navigationMenu);
    }
}


export var NAVIGATION_PROVIDERS = [
    {provide: NavigationService, useClass: NavigationService}
];