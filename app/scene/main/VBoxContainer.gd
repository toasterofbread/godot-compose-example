extends VBoxContainer

var count: int = 0

func _on_button_pressed():
	count += 1
	$Button.text = "Pressed %d times" % count
