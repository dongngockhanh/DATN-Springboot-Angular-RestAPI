export class PaymentDTO {
    amount: number;
    constructor(data: any) {
        this.amount = data.amount;
    }
}