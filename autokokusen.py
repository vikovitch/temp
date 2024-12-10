import keyboard
import time

# Configuration: Set the key and interval here
key_to_press = "3"  # Key you want pressed
key_to_hold = "e"  # Key to hold
interval = 0.3  # Interval in seconds between presses


def auto_press_key_while_holding():
    print(f"Hold '{key_to_hold}' to auto-press '{key_to_press}' every {interval} seconds.")

    try:
        while True:
            # Check if key is being held
            if keyboard.is_pressed(key_to_hold):
                keyboard.press_and_release(key_to_press)
                time.sleep(interval)
            else:
                time.sleep(0.1)  # Small delay to avoid high CPU usage while idle
    except KeyboardInterrupt:
        print("Program interrupted. Exiting...")


if __name__ == "__main__":
    auto_press_key_while_holding()
