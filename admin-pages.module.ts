import {CUSTOM_ELEMENTS_SCHEMA, NgModule, Optional, SkipSelf} from "@angular/core";
import {RouterModule} from "@angular/router";
import {Dashboard} from "./dashboard/dashboard";
import {DevButtonsPage} from "./developer-sandbox/dev-buttons";
import {DevMetersPage} from "./developer-sandbox/dev-meters";
import {LogsPage} from "./diagnostics/logs-page";
import {SystemResetPage} from "./diagnostics/system-reset-page";
import {CloudPage} from "./general-settings/cloud-page";
import {DateTimePage} from "./general-settings/date-time-page";
import {MirroringPage} from "./general-settings/mirroring-page";
import {MonitorPage} from "./general-settings/monitor-page";
import {NamesPage} from "./general-settings/names-page";
import {PairingPage} from "./general-settings/pairing-page";
import {RegionLanguagePage} from "./general-settings/region-language-page";
import {DnsPage} from "./network-settings/dns-page";
import {LanOptionsPage} from "./network-settings/lan-options-page";
import { WifiOptionsPage } from "./network-settings/wifi-options-page";
import { CallSettingsPage } from "./call-configuration/call-settings-page";
import { RecentCallsPage } from "./call-configuration/recent-calls-page";
import {LocalAccountsPage} from "./security/local-accounts";
import {WirelessPage} from "./security/wireless";
import {UniversalModule} from "../universal.module";
import {PFormsModule} from "./common/form/p-forms.module";
import {PControlsModule} from "./common/controls/p-controls.module";
import {PNavigationModule} from "./common/nav/p-navigation.module";
import {PContainersModule} from "./common/containers/p-containers.module";
import {SystemStatusPage} from "./diagnostics/status-page";
import {IpAddressPage} from "./network-settings/ip-address-page";
import {DevFormsPage} from "./developer-sandbox/dev-forms";
import {SoftwareUpdatePage} from "./general-settings/soft-update-page";
import {SecurityCodePage} from "./security/security-code";
import {ProvisioningServerPage} from "./general-settings/provisioning-server-page";
import {CertificatesPage} from "./security/certificates-page";
import {CertRequestForm} from "./security/cert-request-form";
import { GlobalSecurityPage } from "./security/global-security-page";
import {CastingAppModule} from "./developer-sandbox/youtube-casting/casting-app.module";
import {CertificateTable} from "./security/certificate-table";
import {H323Page} from "./network-settings/h323-page";
import {SIPPage} from "./network-settings/sip-page";
import { NetworkQualityPage } from "./network-settings/network-quality-page";
import {DevColorsPage} from "./developer-sandbox/dev-colors";
import {ColorPickerModule} from "primeng/primeng";
import {DialerComponent} from './conferencing/dialer.component';
import { DialingPreferencePage } from "./call-configuration/dialing-preference-page";
import {DirectoryServerPage} from "./general-settings/directory-server-page";
import {DevRestPage} from './developer-sandbox/dev-rest';
import {ConferenceInfoComponent} from "./conferencing/conference-info.component";
import { CalendarServerPage } from "./general-settings/calendar-server-page";
import { ConferenceInfoComponent } from "./conferencing/conference-info.component";
import {TerminalInfoComponent} from "./conferencing/terminal-info.component";
import {ConnectionInfoComponent} from "./conferencing/connection-info.component";
import {DevStylesPage} from "./developer-sandbox/dev-styles";
import { DevGeneratorsPage } from "./developer-sandbox/dev-generators";
import { SnmpPage } from "./servers/snmp-page";
import { PFormComponentModule } from "./common/form/p-form/p-form-component.module";
import { TimeInCallPage } from "./call-configuration/time-in-call-page";
import 'prismjs/prism';
import 'prismjs/components/prism-typescript';
import {PrismComponent} from "angular-prism";

@NgModule({
    imports: [
        UniversalModule,
        RouterModule,
        PContainersModule,
        PNavigationModule,
        PControlsModule,
        PFormsModule,
        PFormComponentModule,
        CastingAppModule,
        ColorPickerModule
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA],
    declarations: [
        CalendarServerPage,
        CallSettingsPage,
        CertificatesPage,
        CertificateTable,
        CertRequestForm,
        CloudPage,
        ConferenceInfoComponent,
        ConnectionInfoComponent,
        Dashboard,
        DateTimePage,
        DevButtonsPage,
        DevGeneratorsPage,
        DevColorsPage,
        DevFormsPage,
        DevMetersPage,
        DevStylesPage,
        DevRestPage,
        DialerComponent,
        DialingPreferencePage,
        DirectoryServerPage,
        CalendarServerPage,
        DnsPage,
        GlobalSecurityPage,
        H323Page,
        DialingPreferencePage,
        IpAddressPage,
        LanOptionsPage,
        LocalAccountsPage,
        LogsPage,
        MirroringPage,
        MonitorPage,
        NamesPage,
        NetworkQualityPage,
        PairingPage,
        ProvisioningServerPage,
        RecentCallsPage,
        RegionLanguagePage,
        SecurityCodePage,
        SIPPage,
        DialerComponent,
        SnmpPage,
        SoftwareUpdatePage,
        SystemResetPage,
        SystemStatusPage,
        TerminalInfoComponent,
        WifiOptionsPage,
        TimeInCallPage,
        WirelessPage,
        PrismComponent
    ],
    exports: [
        CallSettingsPage,
        CloudPage,
        Dashboard,
        DateTimePage,
        DevGeneratorsPage,
        DevButtonsPage,
        DevColorsPage,
        DevFormsPage,
        DevMetersPage,
        DevStylesPage,
        DevRestPage,
        DialerComponent,
        DnsPage,
        H323Page,
        IpAddressPage,
        LanOptionsPage,
        LocalAccountsPage,
        LogsPage,
        MirroringPage,
        MonitorPage,
        NamesPage,
        NetworkQualityPage,
        PairingPage,
        ProvisioningServerPage,
        CalendarServerPage,
        RecentCallsPage,
        RegionLanguagePage,
        SecurityCodePage,
        SnmpPage,
        SIPPage,
        DialerComponent,
        SoftwareUpdatePage,
        SystemResetPage,
        SystemStatusPage,
        WifiOptionsPage,
        WirelessPage
    ]
})

export class AdminPagesModule {
    constructor(@Optional() @SkipSelf() parentModule: AdminPagesModule) {
        if (parentModule) {
            throw new Error(`AdminPagesModule is already loaded.  Import it in the bootstrap module only!`);
        }
    }
}
