import { Injectable } from '@angular/core';
import { HttpHandler, HttpInterceptor, HttpRequest } from '@angular/common/http';
import { MsAdalAngular6Service } from 'microsoft-adal-angular6';
import { mergeMap } from 'rxjs/operators';

@Injectable()
export class AuthInterceptor implements HttpInterceptor {

  constructor(private adal: MsAdalAngular6Service) {
  }

  intercept(req: HttpRequest<any>, next: HttpHandler) {
    const resource = this.adal.GetResourceForEndpoint(req.url);
    if (!resource) {
      return next.handle(req);
    }

    return this.adal.acquireToken(req.url).pipe(
      mergeMap((token: string) => {
        const authorizedRequest = req.clone({
          headers: req.headers.set('Authorization', 'Bearer ' + token),
        });
        return next.handle(authorizedRequest);
      }));
  }
}
