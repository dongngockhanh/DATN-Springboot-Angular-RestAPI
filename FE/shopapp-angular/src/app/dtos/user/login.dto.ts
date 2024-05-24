import{
    IsEmail,
    IsNotEmpty,
    IsPhoneNumber,
    IsString,
    MaxLength,
    MinLength
} from 'class-validator';
export class LoginDTO{
    @IsNotEmpty()
    @IsPhoneNumber()
    phone_number: string;

    @IsNotEmpty()
    @IsString()
    password: string;

    constructor(data:any){
        this.phone_number = data.phoneNumber;
        this.password = data.password;
    }
}