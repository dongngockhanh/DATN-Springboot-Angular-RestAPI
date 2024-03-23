import{
    IsString,
    IsEmail,
    IsNotEmpty,
    IsNumber,
    IsOptional,
    IsDate,
    IsPhoneNumber
} from 'class-validator';

export class RegisterDTO{
    @IsString()
    @IsNotEmpty()
    fullname:string;

    // @IsPhoneNumber()
    @IsString()
    @IsNotEmpty()
    phone_number:string;

    // @IsEmail()
    email:string;

    @IsString()
    @IsNotEmpty()
    password:string;

    @IsString()
    @IsNotEmpty()
    retype_password:string;

    @IsString()
    address:string;

    // @IsDate()
    @IsOptional()
    date_of_birth:Date|null;
    facebook_id:number = 0;
    google_id:number = 0;
    role_id:number = 1;
    
    constructor(data:any){
        this.fullname = data.fullname;
        this.phone_number = data.phone_number;
        this.email = data.email||'';
        this.password = data.password;
        this.retype_password = data.retype_password;
        this.address = data.address||'';
        this.date_of_birth = data.date_of_birth||null;
        this.facebook_id = data.facebook_id||0;
        this.google_id = data.google_id||0;
        this.role_id = data.role_id||1;
    }
}