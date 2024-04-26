class_name AndroidViewNode
extends TextureRect

const IMAGE_FORMAT: Image.Format = Image.Format.FORMAT_RGBA8
const PLUGIN_NAME: String = "ComposeAndroidPlugin"

@export var view_data: String = ""

var plugin: Object = null

var view_index: int = -1
var width: int = -1
var height: int = -1

var image: Image = null
var image_texture: ImageTexture = null

func _ready():
	if Engine.has_singleton(PLUGIN_NAME):
		plugin = Engine.get_singleton(PLUGIN_NAME)
	
	if plugin == null:
		visible = false
		return
	
	updateSize()
	
	view_index = plugin.createView(width, height)
	if visible:
		plugin.enableView(view_index, view_data)
	
	gui_input.connect(onGuiInput)
	resized.connect(onResized)

func _process(_delta: float):
	if not visible:
		return
	
	var draw_bytes: PackedByteArray = plugin.viewDraw(view_index)["data"]
	image.set_data(width, height, false, IMAGE_FORMAT, draw_bytes)
	image_texture.update(image)

func onGuiInput(event: InputEvent):
	if is_instance_of(event, InputEventMouse):
		plugin.viewOnTouchEvent(view_index, event.button_mask, event.position.x, event.position.y)

func onResized():
	updateSize()
	plugin.viewSetSize(view_index, width, height)

func updateSize():
	width = max(1, round(size.x))
	height = max(1, round(size.y))
	
	image = Image.create(width, height, false, IMAGE_FORMAT)
	image_texture = ImageTexture.create_from_image(image)
	texture = image_texture
