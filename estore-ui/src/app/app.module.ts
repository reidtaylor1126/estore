import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { TopbarComponent } from './components/topbar/topbar.component';
import { ProductsComponent } from './components/products/products.component';
import { ProductComponent } from './components/product/product.component';
import { NotFoundComponent } from './components/not-found/not-found.component';
import { HttpClientModule } from '@angular/common/http';
import { ProductDetailComponent } from './components/product-detail/product-detail.component';
import { MobileNavComponent } from './components/mobile-nav/mobile-nav.component';
import { AccountNavComponent } from './components/account-nav/account-nav.component';
import { LoginComponent } from './components/login/login.component';
import { FormsModule } from '@angular/forms';
import { RegisterComponent } from './components/register/register.component';
import { CreateProductComponent } from './components/create-product/create-product.component';
import { CartComponent } from './components/cart/cart.component';
import { TransactionOverviewComponent } from './components/transaction-overview/transaction-overview.component';
import { TransactionProductComponent } from './components/transaction-product/transaction-product.component';
import { AdminOrdersComponent } from './components/admin-orders/admin-orders.component';
import { AdminOrderComponent } from './components/admin-order/admin-order.component';
import { OrderProductComponent } from './components/order-product/order-product.component';
import { TransactionCompleteComponent } from './components/transaction-complete/transaction-complete.component';

@NgModule({
    declarations: [
        AppComponent,
        TopbarComponent,
        ProductsComponent,
        ProductComponent,
        NotFoundComponent,
        ProductDetailComponent,
        MobileNavComponent,
        AccountNavComponent,
        LoginComponent,
        RegisterComponent,
        CreateProductComponent,
        CartComponent,
        TransactionOverviewComponent,
        TransactionProductComponent,
        AdminOrdersComponent,
        AdminOrderComponent,
        OrderProductComponent,
        TransactionCompleteComponent,
    ],
    imports: [BrowserModule, AppRoutingModule, HttpClientModule, FormsModule],
    providers: [],
    bootstrap: [AppComponent],
})
export class AppModule {}
