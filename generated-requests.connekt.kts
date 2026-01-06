val host = "http://localhost:8080/api"

POST("$host/orders") {
    header("Content-Type", "application/json")
    body(
        """
        {
            "customerId": 23,
            "address": "pushkina 10",
            "items": [
                {
                  "itemId": 34,
                  "quantity": 4,
                  "name": "cream soup"
                },
                {
                    "itemId": 35,
                    "quantity": 2,
                    "name": "pizza"
                }
            ]
        }
        """.trimIndent()
    )
}


GET("$host/orders/{id}") {
    pathParam("id", "3")

}


POST("$host/orders/{id}/pay") {
    pathParam("id", "4")
    header("Content-Type", "application/json")
    body(
        """
        {
            "paymentMethod": "QR"
        }
        """.trimIndent()
    )

}
