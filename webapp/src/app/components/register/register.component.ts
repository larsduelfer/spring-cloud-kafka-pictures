import { Component, OnInit } from '@angular/core';
import { FormControl, Validators, FormBuilder, FormGroup } from '@angular/forms';
import { Router } from '@angular/router';
import { Store } from '@ngxs/store';
import { UpdateUserAction } from '../../states/current-user.state';
import { RegisterStateModel, RegisterInitAction } from '../../states/register.state';
import { UserResource } from '../../resources/user/UserResource';

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.sass']
})
export class RegisterComponent implements OnInit {

  protected firstNameFormControl = new FormControl('', [Validators.required, Validators.maxLength(256)]);

  protected lastNameFormControl = new FormControl('', [Validators.required, Validators.maxLength(256)]);

  protected displayNameFormControl = new FormControl('', [Validators.required, Validators.maxLength(256)]);

  protected registerFormGroup = new FormGroup({
    firstName: this.firstNameFormControl,
    lastName: this.lastNameFormControl,
    displayName: this.displayNameFormControl
  });

  constructor(private router: Router, private store: Store, private formBuilder: FormBuilder) {
    this.store.select(state => state.currentUser).subscribe(currentUser => {
      this.store.dispatch(new RegisterInitAction(currentUser as UserResource)).subscribe(_ => {
        //Initialize form controls with existing values.
        this.firstNameFormControl.setValue(currentUser.firstName);
        this.lastNameFormControl.setValue(currentUser.lastName);
        this.displayNameFormControl.setValue(currentUser.displayName);

        //Validate values
        Object.keys(this.registerFormGroup.controls).forEach(field => {
          const control = this.registerFormGroup.get(field);
          if (control instanceof FormControl) {
            control.markAsTouched({ onlySelf: true });
          }
        });
      });
    });
  }

  ngOnInit() { }

  sendRegistration() {
    if (this.registerFormGroup.valid) {
      //Take current snapshot when the button is clicked and update the current user
      let currentUser = (this.store.snapshot().register as RegisterStateModel).registerForm.model;
      this.store.dispatch(new UpdateUserAction(currentUser.firstName, currentUser.lastName, currentUser.displayName)).subscribe(_ => {
        this.store.dispatch(new RegisterInitAction()).subscribe(_ => {
          this.router.navigateByUrl('/');
        });
      });
    }
  }

}
