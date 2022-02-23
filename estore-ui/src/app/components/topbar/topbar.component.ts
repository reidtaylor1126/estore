import { Component, OnInit } from '@angular/core';

@Component({
    selector: 'app-topbar',
    templateUrl: './topbar.component.html',
    styleUrls: ['./topbar.component.css'],
})
export class TopbarComponent implements OnInit {
    navOpen = false;
    constructor() {}

    ngOnInit(): void {}

    toggleNav(value?: boolean): void {
        if (typeof value !== 'undefined') {
            this.navOpen = value;
            return;
        }
        this.navOpen = !this.navOpen;
    }
}
