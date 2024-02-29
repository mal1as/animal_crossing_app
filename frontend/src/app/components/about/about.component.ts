import { Component} from '@angular/core';

@Component({
  selector: 'app-about',
  templateUrl: './about.component.html'
})

export class AboutComponent {
  public isLightTheme = true;

  onThemeSwitchChange() {
    this.isLightTheme = !this.isLightTheme;

    document.body.setAttribute(
        'class',
        this.isLightTheme ? "bg-dark text-light text-monospace h5" : "bg-light text-dark text-monospace h5"
    );
  }
}



