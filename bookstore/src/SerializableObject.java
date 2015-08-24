public class SerializableObject {
	private Object object;

	public SerializableObject() {

	}

	public SerializableObject(Object object) {
		this.object = object;
	}

	public void setObject(Object object) {
		this.object = object;
	}

	public Object getObject() {
		return object;
	}
}
