import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';

@Component({
    selector: 'app-mobile-nav',
    templateUrl: './mobile-nav.component.html',
    styleUrls: ['./mobile-nav.component.css'],
})
export class MobileNavComponent implements OnInit {
    @Input() navOpen: boolean = false;
    @Output() toggleMenu = new EventEmitter<boolean>();
    constructor() {}

    ngOnInit(): void {}

    toggleNav(value: boolean) {
        this.toggleMenu.emit(value);
    }
}
