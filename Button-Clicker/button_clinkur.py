import pyautogui
import time
import cv2
import numpy as np
from PIL import ImageGrab
import keyboard

# Constants
CHECK_INTERVAL = 0  # Time between checks (in seconds)
WAIT_BEFORE_CLICK = 0.02  # Time to wait before clicking after moving (in seconds)
CLICK_WAIT_ACTIVE = False  # Should it pause at all
MAX_FAILURES = 10  # Number of consecutive failures before stopping
MIN_DISTANCE_THRESHOLD = 4  # Minimum distance in pixels to trigger a click (set your preferred value)
START_KEY = "f"

IMAGE_ADDRESS = "C:\\Users\\thijs\PycharmProjects\\pysong\\Button-Clicker\\button_image.png"

# Global failure count
failure_count = 0
last_button_position = None  # To keep track of the last button position


# Wait for the user to press the designated start key
def wait_for_start():
    print(f"Press '{START_KEY}' to start or resume the loop...")
    keyboard.wait(START_KEY)  # Wait for the key to be pressed
    print(f"'{START_KEY}' pressed. Starting or resuming the loop...")

# Function to capture the screen and check for button
def find_button(button_template):
    screen = np.array(ImageGrab.grab())
    screen_gray = cv2.cvtColor(screen, cv2.COLOR_BGR2GRAY)

    # Perform template matching
    result = cv2.matchTemplate(screen_gray, button_template, cv2.TM_CCOEFF_NORMED)
    threshold = 0.7
    locations = np.where(result >= threshold)

    # Check if any match is found
    if len(locations[0]) > 0:
        return locations[1][0], locations[0][0], button_template.shape[1], button_template.shape[0]
    else:
        return None

# Function to calculate the Euclidean distance between two points
def calculate_distance(pos1, pos2):
    x1, y1 = pos1
    x2, y2 = pos2
    return ((x2 - x1) ** 2 + (y2 - y1) ** 2) ** 0.5

# Function to click the button at the given position
def click_button(position, template_size):
    global last_button_position

    # If a button has been found previously and the new position is too close, skip the click
    if last_button_position:
        distance = calculate_distance(last_button_position, position)
        if distance < MIN_DISTANCE_THRESHOLD:
            print(f"Button detected at {position}, but it's too close to the last position. Skipping click.")
            return  # Skip clicking if the new position is too close to the last one

    center_x, center_y = position
    template_width, template_height = template_size

    # Calculate the center of the button
    center_x = center_x + template_width // 2
    center_y = center_y + template_height // 2

    # Move the mouse to the center of the button
    pyautogui.moveTo(center_x, center_y)

    # Wait for the specified time before clicking
    if CLICK_WAIT_ACTIVE:
        time.sleep(WAIT_BEFORE_CLICK)

    # Perform the click
    pyautogui.click()

    # Print confirmation
    print(f"Clicked at position: {center_x}, {center_y}")

    # Update the last button position
    last_button_position = position

# Main loop for continuous checking and clicking
def main(button_template):
    global failure_count

    # Wait for the start key press
    wait_for_start()

    while True:
        # Find the button position
        result = find_button(button_template)

        if result:
            # If a button is found, reset failure count and click
            failure_count = 0
            print("Button found!")
            click_button(result[:2], result[2:])
        else:
            # If no button is found, increment the failure count
            failure_count += 1
            print(f"No button detected. Failure count: {failure_count}")

        # If failure count reaches maximum, wait for user to press the start key again
        if failure_count >= MAX_FAILURES:
            print(f"Maximum consecutive failures reached. Please press '{START_KEY}' to resume.")
            failure_count = 0  # Reset the failure count
            wait_for_start()  # Wait for user to press the start key again

        # Wait for the next check
        time.sleep(CHECK_INTERVAL)

# Example usage
if __name__ == "__main__":
    # Load the button template (ensure this is correct)
    button_template = cv2.imread(IMAGE_ADDRESS, cv2.IMREAD_GRAYSCALE)
    
    # Run the main loop
    main(button_template)