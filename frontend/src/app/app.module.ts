import {BrowserModule} from '@angular/platform-browser';
import {NgModule} from '@angular/core';
import {RadioButtonModule} from 'primeng/radiobutton';
import {InputTextModule} from 'primeng/inputtext';
import {FormsModule, ReactiveFormsModule} from '@angular/forms';
import {ButtonModule} from 'primeng/button';
import {TableModule} from 'primeng/table';
import {AboutComponent} from './components/about/about.component';
import {Routes, RouterModule} from '@angular/router';
import {HttpClientModule} from '@angular/common/http';
import {LoginComponent} from './components/login/login.component';
import {RegisterComponent} from './components/register/register.component';
import {AnimalCrossingGuard} from './guard/animal-crossing-guard.service';
import {SidebarModule} from 'primeng/sidebar';
import {BrowserAnimationsModule} from '@angular/platform-browser/animations';
import {ListboxModule} from "primeng/listbox";

import {AppComponent} from './components/main/app.component';
import {HomeComponent} from "./components/home/home.component";
import {ExperimentComponent} from "./components/ecperiment/experiment.component";
import {AddReportComponent} from "./components/addReport/addReport.component";
import {InputNumberModule} from "primeng/inputnumber";
import {CalendarModule} from "primeng/calendar";
import {ReportHistoryComponent} from "./components/reportHistory/reportHistory.component";
import {ExperimentHistoryComponent} from "./components/experimentHistory/experimentHistory.component";
import {AnimalComponent} from "./components/animal/animal.component";
import {MoveToReserveComponent} from "./components/moveToReserve/moveToReserve.component";

const appRoutes: Routes = [
    {path: 'home', component: HomeComponent, canActivate: [AnimalCrossingGuard]},
    {path: 'login', component: LoginComponent, canActivate: [AnimalCrossingGuard]},
    {path: 'register', component: RegisterComponent, canActivate: [AnimalCrossingGuard]},
    {path: 'experiment', component: ExperimentComponent, canActivate: [AnimalCrossingGuard]},
    {path: 'addReport', component: AddReportComponent, canActivate: [AnimalCrossingGuard]},
    {path: 'reportHistory', component: ReportHistoryComponent, canActivate: [AnimalCrossingGuard]},
    {path: 'experimentHistory', component: ExperimentHistoryComponent, canActivate: [AnimalCrossingGuard]},
    {path: 'animal', component: AnimalComponent, canActivate: [AnimalCrossingGuard]},
    {path: 'moveToReserve', component: MoveToReserveComponent, canActivate: [AnimalCrossingGuard]},
    {path: '**', redirectTo: 'login'}
];

@NgModule({
    declarations: [
        AppComponent,
        LoginComponent,
        RegisterComponent,
        AboutComponent,
        HomeComponent,
        ExperimentComponent,
        AddReportComponent,
        ReportHistoryComponent,
        ExperimentHistoryComponent,
        AnimalComponent,
        MoveToReserveComponent
    ],
    imports: [
        RouterModule.forRoot(appRoutes),
        BrowserModule,
        RadioButtonModule,
        InputTextModule,
        FormsModule,
        ButtonModule,
        ReactiveFormsModule,
        TableModule,
        HttpClientModule,
        SidebarModule,
        BrowserAnimationsModule,
        ListboxModule,
        InputNumberModule,
        CalendarModule
    ],
    providers: [],
    bootstrap: [AppComponent]
})
export class AppModule {
}
