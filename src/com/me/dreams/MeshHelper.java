package com.me.dreams;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.loaders.ModelLoader;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.loader.ObjLoader;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Matrix3;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;

public class MeshHelper {
	protected Mesh mesh;
	protected ShaderProgram meshShader;
	
	static Matrix4 viewMatrix;
	static Matrix4 projMatrix;
	
	static Vector3 lightPos;
	static Vector3 lightColour;
	static float   lightRadius;
	
	static Vector3 cameraPos;
	
	Vector3 position;
	Vector3 colour;
	Vector3 scale;
	Vector3 rotationAxis;
	float	degrees;
	
	static float	hueSeconds;			//Each time you play the game object colours should be different :D
	static float	bgHour;				//The sky should reflect the outside :D Night time day time sun set all that shite.
	
	static float	tic;
	static float	drawMadBG;
	
	Texture tex;
	
	Matrix4 modelMatrix;
	Matrix3 normalMatrix;
	Matrix4 textureMatrix;
	
	public void CreateMesh(float[] vertices) {
		mesh = new Mesh(true,vertices.length, 0,
				new VertexAttribute(Usage.Position, 3, "position"),
				new VertexAttribute(Usage.TextureCoordinates, 2, "texCoord"),
				new VertexAttribute(Usage.Normal, 3, "normal"));
		
		mesh.setVertices(vertices);
		
		setupValues();
		
		//CreateShader();
	}
	
	public void setTex(String filename){
		//tex = new Texture(Gdx.files.internal(filename));
		tex = Dreams_Of_Dishes.assetManager.get(filename,Texture.class);
	}
	
	
	
	
	//TO DO!
	public void LoadOBJMesh(String filename){

        @SuppressWarnings("rawtypes")
		ModelLoader loader = new ObjLoader();
        Model model = loader.loadModel(Gdx.files.internal(filename));
        
        int numVertices = model.meshes.first().getNumVertices();

        mesh = new Mesh(true,numVertices, 0,
				new VertexAttribute(Usage.Position, 3, "position"),
				new VertexAttribute(Usage.TextureCoordinates, 2, "texCoord"),
				new VertexAttribute(Usage.Normal, 3, "normal"));
        
        float[] vertexList = new float[numVertices*8];
        
        model.meshes.first().getVertices(vertexList);
        
        
        setupValues();
        
        mesh.setVertices(vertexList);
       
	}
	
	void setupValues(){
		modelMatrix = new Matrix4();
		normalMatrix= new Matrix3();
		textureMatrix = new Matrix4();
		modelMatrix.idt();
		normalMatrix.idt();
		position 		= new Vector3();
		colour   		= new Vector3(1.0f,1.0f,1.0f);
		scale    		= new Vector3();
		rotationAxis 	= new Vector3();
		degrees			= 0.0f;
		scale.set(1.0f,1.0f,1.0f);
		
		tex = null;
		meshShader = null;
	}
	
	
	public void SetShader(ShaderProgram shader){
		meshShader = shader;
		
	}
	
	public void GameOBJShader(){
		String vertexShader = "attribute vec4 position; \n" 
							+ "attribute vec2 texCoord; \n"
							+ "attribute vec3 normal;   \n"
							+ "uniform mat4 modelMatrix;\n"
							+ "uniform mat4 viewMatrix; \n"  
							+ "uniform mat4 projMatrix; \n"
							+ "uniform mat3 normalMatrix; \n"
							+ "varying vec2 v_texCoord; \n"
							+ "varying vec3 v_normal;   \n"
							+ "varying vec3 worldPos;   \n"
							+ "varying vec3 v_colour;   \n"

							+   "uniform vec3 colour;	  \n"
							

 							+	"uniform float hueSeconds;"
							
							//HUE SWITCH ;)							
							+"vec3 hueSwitch(vec3 c, float hueValue) {"
							+"	vec3 retValue;"
							+"	vec4 K = vec4(0.0, -1.0 / 3.0, 2.0 / 3.0, -1.0);"
							+"    vec4 p = mix(vec4(c.bg, K.wz), vec4(c.gb, K.xy), step(c.b, c.g));"
							+"    vec4 q = mix(vec4(p.xyw, c.r), vec4(c.r, p.yzx), step(p.x, c.r));"
							+"float d = q.x - min(q.w, q.y);"
							+"    float e = 1.0e-10;"
							+"    vec3 hsv = vec3(abs(q.z + (q.w - q.y) / (6.0 * d + e)), d / (q.x + e), q.x);"
							+"	hsv.x += hueValue;"
							+"	K = vec4(1.0, 2.0 / 3.0, 1.0 / 3.0, 3.0);"
							+"    vec3 p2 = abs(fract(hsv.xxx + K.xyz) * 6.0 - K.www);"
							+"    vec3 temp =   vec3(hsv.z * mix(K.xxx, clamp(p2 - K.xxx, 0.0, 1.0), hsv.y));"
							+"	retValue = temp.rgb;"
							+"	return retValue;"
							+"}"
							
							
							
							
							+ "void main() {            \n"
							+ "vec4 tempPos = position; \n"
							+ "tempPos.w = 1.0; 		\n"
							+ "v_texCoord = texCoord;   \n"
							+ "v_normal   = normalize(normalMatrix  * normalize(normal) ) ;     \n"
							+ "v_colour = hueSwitch(colour, hueSeconds); \n"
							+ "worldPos = (modelMatrix * tempPos).xyz; \n"
							+ "gl_Position = (projMatrix * viewMatrix * modelMatrix) * tempPos;  \n"
							+"}";
		
		String fragmentShader = "#ifdef GL_ES			  \n"
							+   "precision mediump float; \n"
							+	"#endif					  \n"

 							+	"uniform vec3  cameraPos ; \n"
 							+	"uniform vec3  lightColour ;\n"
 							+	"uniform vec3  lightPos ; \n"
 							+	"uniform float lightRadius ;\n"
 							+ "uniform mat4 normalMatrix; \n"

							+ 	"varying vec3 v_colour;   \n"
							+	"varying vec2 v_texCoord; \n"
							+ 	"varying vec3 v_normal;   \n"
							+   "varying vec3 worldPos;   \n"

							
							+   "void main() {			  \n"
							
							+   "vec4 diffuse = vec4(v_colour.rgb, 1.0);"
							+	"vec3 incident = normalize ( lightPos - worldPos ); \n"
							+   "float lambert = max (0.0 , dot ( incident , v_normal )); \n"
							+	"float dist = length ( lightPos - worldPos ); \n"
							+	"float atten = 1.0 - clamp ( dist / lightRadius , 0.0 , 1.0); \n"
							+	"vec3 viewDir = normalize ( cameraPos - worldPos ); \n"
							+	"vec3 halfDir = normalize ( incident + viewDir );\n"
							+	"float rFactor = max (0.0 , dot ( halfDir , v_normal ));"
							
							+	"vec3 colour = ( diffuse.rgb * lightColour.rgb );\n"
							
							+	"gl_FragColor = vec4 ( colour * atten * lambert , diffuse . a );\n"
							+   "gl_FragColor . rgb += colour * 0.33 \n;"
							+	"}";
		
		meshShader = new ShaderProgram(vertexShader,fragmentShader);
		
		if(!meshShader.isCompiled()){
			throw new IllegalStateException(meshShader.getLog());
		}
		
	}
	
	public void GameHUDShader(){
		String vertexShader = "attribute vec4 position; \n" 
							+ "attribute vec2 texCoord; \n"
							+ "attribute vec3 normal;   \n"
							+ "uniform mat4 modelMatrix;\n"
							+ "uniform mat4 viewMatrix; \n"  
							+ "uniform mat4 projMatrix; \n"
							+ "varying vec2 v_texCoord; \n"
							+ "varying vec3 v_normal;   \n"
							+ "void main() {            \n"
							+ "vec4 tempPos = position; \n"
							+ "tempPos.w = 1.0; 		\n"
							+ "v_texCoord = texCoord;   \n"
							+ "v_normal   = normal;     \n"
							+ "gl_Position = (projMatrix * viewMatrix * modelMatrix) * tempPos;  \n"
							+"}";
		
		String fragmentShader = "#ifdef GL_ES			  \n"
							+   "precision mediump float; \n"
							+	"#endif					  \n"
							+   "uniform vec3 colour;	  \n"
							+	"varying vec2 v_texCoord; \n"
							+ 	"varying vec3 v_normal;   \n"
							+   "void main() {			  \n"
							+	"gl_FragColor = vec4(colour, 1.0); \n"
							+	"}";
		
		meshShader = new ShaderProgram(vertexShader,fragmentShader);
		
		if(!meshShader.isCompiled()){
			throw new IllegalStateException(meshShader.getLog());
		}
		
	}
	
	public void RainbowShader(){
		String vertexShader = "attribute vec4 position; \n" 
							+ "attribute vec2 texCoord; \n"
							+ "attribute vec3 normal;   \n"
							+ "uniform mat4 modelMatrix;\n"
							+ "uniform mat4 viewMatrix; \n"  
							+ "uniform mat4 projMatrix; \n"
							+ "uniform float tic; \n"
							+ "varying vec2 v_texCoord; \n"
							+ "varying vec3 v_normal;   \n"
							+ "varying vec3 v_colour;	\n"
							
+"vec3 hueSwitch(vec3 c, float hueValue) {\n"
+"	vec3 retValue;\n"
+"	vec4 K = vec4(0.0, -1.0 / 3.0, 2.0 / 3.0, -1.0);\n"
+"    vec4 p = mix(vec4(c.bg, K.wz), vec4(c.gb, K.xy), step(c.b, c.g));\n"
+"    vec4 q = mix(vec4(p.xyw, c.r), vec4(c.r, p.yzx), step(p.x, c.r));\n"
+"float d = q.x - min(q.w, q.y);\n"
+"    float e = 1.0e-10;\n"
+"    vec3 hsv = vec3(abs(q.z + (q.w - q.y) / (6.0 * d + e)), d / (q.x + e), q.x);\n"
+"	hsv.x += hueValue;\n"
+"	K = vec4(1.0, 2.0 / 3.0, 1.0 / 3.0, 3.0);\n" 
+"    vec3 p2 = abs(fract(hsv.xxx + K.xyz) * 6.0 - K.www);\n"
+"    vec3 temp =   vec3(hsv.z * mix(K.xxx, clamp(p2 - K.xxx, 0.0, 1.0), hsv.y));\n"
+"	retValue = temp.rgb;\n"
+"	return retValue;\n"
+"}\n"
							
							
							
							+ "void main() {            \n"
							+ "vec4 tempPos = position; \n"
							+ "tempPos.w = 1.0; 		\n"
							+ "v_texCoord = texCoord;   \n"
							+ "v_normal   = normal;     \n"
							+ "gl_Position = (projMatrix * viewMatrix * modelMatrix) * tempPos;  \n"
							+ "v_colour = vec3(1.0,1.0,0.5); \n"
							+ "v_colour = hueSwitch(v_colour, (gl_Position.y * 0.01) + tic); \n"
							+"}";
		
		String fragmentShader = "#ifdef GL_ES			  \n"
							+   "precision mediump float; \n"
							+	"#endif					  \n"
							//+   "uniform vec3 colour = vec3(1.0,1.0,1.0);	  \n"
							+	"uniform sampler2D s_texture; \n"
							+	"varying vec2 v_texCoord; \n"
							+ 	"varying vec3 v_normal;   \n"
							+	"varying vec3 v_colour;"
							+	"uniform mat4 textureMatrix;\n"
							+   "void main() {			  \n"
							+	"vec4 temp = vec4(v_texCoord.xy,1.0,1.0); \n"
							+	"temp = textureMatrix * temp;"
							+	"gl_FragColor =  texture2D(s_texture, temp.xy);\n"
							+	"gl_FragColor *= vec4(v_colour, 1.0); \n"
							+	"}";
		
		meshShader = new ShaderProgram(vertexShader,fragmentShader);
		
		if(!meshShader.isCompiled()){
			throw new IllegalStateException(meshShader.getLog());
		}
		
	}
	
	public void TexturedShader(){
		String vertexShader = "attribute vec4 position; \n" 
							+ "attribute vec2 texCoord; \n"
							+ "attribute vec3 normal;   \n"
							+ "uniform mat4 modelMatrix;\n"
							+ "uniform mat4 viewMatrix; \n"  
							+ "uniform mat4 projMatrix; \n"
							+ "varying vec2 v_texCoord; \n"
							+ "varying vec3 v_normal;   \n"
							+ "void main() {            \n"
							+ "vec4 tempPos = position; \n"
							+ "tempPos.w = 1.0; 		\n"
							+ "v_texCoord = texCoord;   \n"
							+ "v_normal   = normal;     \n"
							+ "gl_Position = (projMatrix * viewMatrix * modelMatrix) * tempPos;  \n"
							+"}";
		
		String fragmentShader = "#ifdef GL_ES			  \n"
							+   "precision mediump float; \n"
							+	"#endif					  \n"
							+   "uniform vec3 colour;	  \n"
							+	"uniform sampler2D s_texture; \n"
							+	"varying vec2 v_texCoord; \n"
							+ 	"varying vec3 v_normal;   \n"
							+	"uniform mat4 textureMatrix;\n"
							+   "void main() {			  \n"
							+	"vec4 temp = vec4(v_texCoord.xy,1.0,1.0); \n"
							+	"temp = textureMatrix * temp;"
							+	"gl_FragColor =  texture2D(s_texture, temp.xy);\n"
							+	"gl_FragColor *= vec4(colour, 1.0); \n"
							+	"}";
		
		meshShader = new ShaderProgram(vertexShader,fragmentShader);
		
		if(!meshShader.isCompiled()){
			throw new IllegalStateException(meshShader.getLog());
		}
		
	}
	
	public void bgShader() {
		
		String vertexShader =    
				  "attribute vec4 position; \n" 
				+ "attribute vec2 texCoord; \n"
				+ "attribute vec3 normal;   \n"
				+ "uniform mat4 modelMatrix;\n"
				+ "uniform mat4 viewMatrix; \n"  
				+ "uniform mat4 projMatrix; \n"
				+ "varying vec3 v_normal;   \n"
				+ "varying vec2 v_texCoord; \n"
				+ "varying vec4 v_colour; \n"
				+ 	"uniform float dayRatio; \n"
				+	"vec4 dayTop = vec4(0.0,0.5,1.0,1.0);	\n"
				+	"vec4 dayBottom = vec4(1.0,1.0,1.0,1.0);\n"
				+	"vec4 nightTop = vec4(0.0,0.0,0.5,1.0);	\n"
				+	"vec4 nightBottom = vec4(0.05,0.0,0.05,1.0);\n"
				+	"vec4 sunsetBottom = vec4(1.0,0.5,1.0,1.0);	\n"
				+	"vec4 sunriseBottom = vec4(1.0,1.0,0.5,1.0);	\n"
				
				
				+ "void main() {            \n"
				+ "vec4 tempPos = position; \n"
				+ "tempPos.w = 1.0; 		\n"
				+ "v_texCoord = texCoord;   \n"
				+	"vec4 colourTop = mix(nightTop,dayTop,dayRatio);\n"
				+	"vec4 colourBottom = mix(nightBottom,dayBottom,dayRatio);\n"
				+	"v_colour = mix(colourTop,colourBottom,v_texCoord.y);\n"
				
				+ "gl_Position = (projMatrix * viewMatrix * modelMatrix) * tempPos;  \n"
				+"}";

		String fragmentShader = "#ifdef GL_ES			  \n"
				+   "precision mediump float; \n"
				+	"#endif					  \n"
				+   "uniform vec3 colour;	  \n"
				+ 	"uniform float dayRatio; \n"
				+	"uniform float hueSeconds; \n"
				+	"uniform float isSuper; \n"
				+	"uniform sampler2D s_texture; \n"
				+	"uniform float tic; \n"
				+	"varying vec2 v_texCoord; \n"
				+ 	"varying vec4 v_colour; \n"
				+   "void main() {			  \n"
				+ "vec4 outColour = v_colour; \n"
				+ "vec2 texCoord = v_texCoord;"
				+ "texCoord = vec2(v_texCoord.x + sin(tic * 0.1),v_texCoord.y + cos(tic * 0.1)); "
				//+ "texCoord = vec2(v_texCoord.x,v_texCoord.y); "
				+ "vec4 super =  texture2D(s_texture, texCoord);\n"
				+ "outColour = mix(outColour,super,isSuper); \n"
				+ "gl_FragColor = outColour;"
				+	"}";
		
		meshShader = new ShaderProgram(vertexShader,fragmentShader);
		
		if(!meshShader.isCompiled()){
			throw new IllegalStateException(meshShader.getLog());
		}
		
	}
	
	public void generateQuad() {

		CreateMesh(new float[] {-0.5f, 0.5f, 0.0f,0.0f,0.0f,0.0f,0.0f,-1.0f,
								-0.5f, -0.5f, 0.0f,0.0f,1.0f,0.0f,0.0f,-1.0f,
								0.5f, -0.5f, 0.0f,1.0f,1.0f,0.0f,0.0f,-1.0f,
								0.5f, 0.5f, 0.0f,1.0f,0.0f,0.0f,0.0f,-1.0f,
								-0.5f, 0.5f, 0.0f,0.0f,0.0f,0.0f,0.0f,-1.0f,
								0.5f, -0.5f, 0.0f,1.0f,1.0f,0.0f,0.0f,-1.0f});
	}
	
	public void drawMesh(){
		modelMatrix.idt();
		
		modelMatrix.translate(position);
		modelMatrix.scl(scale);
		modelMatrix.rotate(rotationAxis, degrees);
		
		normalMatrix.idt();
		
		Matrix4 tempMat = modelMatrix.cpy();

		tempMat.tra();
		tempMat.inv();
		
		normalMatrix.set(tempMat);
		
		meshShader.begin();
		
		meshShader.setUniformMatrix(meshShader.getUniformLocation("textureMatrix"), textureMatrix);
		meshShader.setUniformMatrix(meshShader.getUniformLocation("modelMatrix"), modelMatrix);
		meshShader.setUniformMatrix(meshShader.getUniformLocation("viewMatrix"), viewMatrix);
		meshShader.setUniformMatrix(meshShader.getUniformLocation("projMatrix"), projMatrix);
		meshShader.setUniformMatrix(meshShader.getUniformLocation("normalMatrix"), normalMatrix);
		
		meshShader.setUniformf(meshShader.getUniformLocation("lightColour"), lightColour.x, lightColour.y, lightColour.z);
		
		meshShader.setUniformf(meshShader.getUniformLocation("cameraPos"), cameraPos.x, cameraPos.y, cameraPos.z);
		
		meshShader.setUniformf(meshShader.getUniformLocation("lightPos"), lightPos.x, lightPos.y, lightPos.z);
		meshShader.setUniformf(meshShader.getUniformLocation("lightRadius"), lightRadius);
		meshShader.setUniformf(meshShader.getUniformLocation("hueSeconds"), hueSeconds);
		
		float temp = bgHour;
		
		temp = temp / 24.0f;
		temp*= 3.142;
		temp = MathUtils.sin(temp);
		

		meshShader.setUniformf(meshShader.getUniformLocation("tic"), tic);

		meshShader.setUniformf(meshShader.getUniformLocation("isSuper"), drawMadBG);
		
		
		
		meshShader.setUniformf(meshShader.getUniformLocation("dayRatio"), temp);
		
		meshShader.setUniformf(meshShader.getUniformLocation("colour"), colour.x, colour.y, colour.z);
		
		if(tex!=null){
			Gdx.gl20.glActiveTexture(GL20.GL_TEXTURE0);
			Gdx.gl20.glBindTexture(GL20.GL_TEXTURE_2D, tex.getTextureObjectHandle());

			meshShader.setUniformi(meshShader.getUniformLocation("s_texture"),0);
			
		}
		mesh.render(meshShader, GL20.GL_TRIANGLES);
		meshShader.end();
		if(tex!=null){
			Gdx.gl20.glBindTexture(GL20.GL_TEXTURE_2D, 0);			
		}
		
	}
}