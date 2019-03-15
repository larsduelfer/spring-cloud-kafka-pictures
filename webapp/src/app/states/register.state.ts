import { State, Action, StateContext } from '@ngxs/store';
import { UserResource } from '../resources/user/UserResource';

export class RegisterInitAction {
  static readonly type = 'RegisterInit';
  constructor(public userResource?: UserResource) { }
}

export class RegisterStateModel {
  registerForm: RegisterFormModel;
}

export class RegisterFormModel {
  model: RegisterModel;
  dirty: boolean;
  status: string;
  errors: any;
}

export class RegisterModel {
  firstName: string;
  lastName: string;
  displayName: string;
}

@State<RegisterStateModel>({
  name: "register",
  defaults: {
    registerForm: {
      model: {
        firstName: '',
        lastName: '',
        displayName: ''
      },
      dirty: false,
      status: "",
      errors: {}
    }
  }
})
export class RegisterState {

  @Action(RegisterInitAction)
  init(ctx: StateContext<RegisterStateModel>, action: RegisterInitAction) {
    let stateModel = ctx.getState();
    if(action.userResource) {
      stateModel.registerForm.model.displayName = action.userResource.displayName;
      stateModel.registerForm.model.firstName = action.userResource.firstName;
      stateModel.registerForm.model.lastName = action.userResource.lastName;
    } else {
      stateModel.registerForm.model.displayName = '';
      stateModel.registerForm.model.firstName = '';
      stateModel.registerForm.model.lastName = '';
    }
    ctx.setState(stateModel);
  }
}