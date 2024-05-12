import { Role } from "../role/role.reponse"

export interface UserResponse {
    user_id: number
    full_name: string
    phone_number: string
    email: string
    address: string
    is_active: boolean
    date_of_birth: Date
    facebook_id: string
    google_id: string
    role_id: Role
}