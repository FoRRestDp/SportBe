# SportBe

To run this app in `MainActivity.kt` change these lines:
```kotlin
private val email: String = "example@mail.com"
private val password: String = "your_password"
private val device: HealbeDevice = HealbeDevice.create(
        name = "Device name",
        mac = "Device mac",
        pin = "Device pin",
)
```
to suitable for your account and HealBe device.
