Serwer czyta wiadomości z kolejki 'PaymentQueue' (lub kolejki podanej jako zmienna PAYMENT_QUEUE).

Wysyła do odbiorcy określonego w zmiennej properties.reply_to wiadomość, która ma postać:
{
    "status" : HTTPStatus.ACCEPTED
}
jeżeli transakcja się powiodła, natomiast jeśli się nie powiodła to status przyjmuje wartość HTTPStatus.NOT_ACCEPTABLE
