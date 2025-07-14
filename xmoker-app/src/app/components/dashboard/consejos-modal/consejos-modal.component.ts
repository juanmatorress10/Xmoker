import { Component, Input } from '@angular/core';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-consejos-modal',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './consejos-modal.component.html',
  styleUrls: ['./consejos-modal.component.css']
})
export class ConsejosModalComponent {
  @Input() tipoFumador: string = '';
  consejosSeleccionados: { categoria: string, consejo: string }[] = [];

  private bancoConsejos: { [categoria: string]: string[] } = {
    "Estimulacion": [
      "⚡ Toma pausas activas breves durante el trabajo para recargar energía sin necesidad de fumar.",
      "⚡ Haz 3 respiraciones profundas al sentirte cansado: oxigenarte activa tu concentración.",
      "⚡ Escucha música enérgica para mantenerte motivado durante tareas exigentes.",
      "⚡ Camina 5 minutos al aire libre para despejar tu mente en vez de buscar un cigarro.",
      "⚡ Usa apps de productividad con pausas programadas para evitar momentos de fatiga.",
      "⚡ Haz una caminata corta al aire libre para reactivar cuerpo y mente.",
      "⚡ Realiza estiramientos de cuello y espalda si te notas tenso o cansado.",
      "⚡ Toma agua fría con unas gotas de limón para despertar tus sentidos.",
      "⚡ Usa aromas cítricos o mentolados en tu espacio de trabajo para mantenerte alerta.",
      "⚡ Haz 10 sentadillas o movimientos rápidos para despejarte sin salir.",
      "⚡ Cambia de tarea si sientes que tu energía baja: romper la rutina estimula.",
      "⚡ Haz pausas visuales: mira por la ventana o cambia el punto de enfoque cada 30 minutos.",
      "⚡ Configura un temporizador para alternar 25 minutos de trabajo y 5 de recarga (técnica Pomodoro).",
      "⚡ Come una fruta como manzana o naranja: su frescura y sabor activan.",
      "⚡ Escucha una playlist de concentración en momentos clave del día.",
      "⚡ Usa una botella de agua visible y colorida como recordatorio para hidratarte y mantenerte en marcha.",
      "⚡ Escribe tus logros del día: repasar lo conseguido te motiva más que un cigarro.",
      "⚡ Aplica un poco de agua fría en tu cara o muñecas para despejarte al instante.",
      "⚡ Cambia de posición al trabajar: ponte de pie o siéntate distinto.",
      "⚡ Haz ejercicios de respiración energética: inhala fuerte por la nariz y exhala por la boca.",
      "⚡ Camina mientras hablas por teléfono o escuchas un podcast.",
      "⚡ Coloca frases estimulantes en tu escritorio o fondo de pantalla.",
      "⚡ Baila un par de minutos si estás solo: el movimiento sube tu energía sin nicotina.",
      "⚡ Juega brevemente con una pelota antiestrés para activar tu cuerpo sin fumar.",
      "⚡ Date un microrecompensa no relacionada al tabaco tras una tarea cumplida: ¡lo mereces!"
    ],
      "Refuerzo gestual": [
    "👐 Lleva contigo un objeto antiestrés como una goma, piedra lisa o bolígrafo especial.",
    "👐 Usa chicles, caramelos sin azúcar o palillos para tener algo en la boca.",
    "👐 Sostén una taza caliente o una botella reutilizable mientras conversas.",
    "👐 Manipula una pelota de goma, plastilina o anillo giratorio durante el día.",
    "👐 Haz pequeños dibujos o garabatos cuando estés inquieto o esperando.",
    "👐 Cepíllate los dientes después de las comidas para cortar el hábito gestual.",
    "👐 Usa pajitas para beber agua o zumo: simula el gesto pero de forma sana.",
    "👐 Ten un cuaderno o libreta pequeña para hacer listas o escribir pensamientos.",
    "👐 Utiliza una aplicación de relajación que implique tocar la pantalla o respirar con ritmo.",
    "👐 Haz estiramientos de manos y muñecas: activa y relaja a la vez.",
    "👐 Juega con un llavero, clip, cordón o cadena discreta que puedas manipular.",
    "👐 Practica origami o doblado de papel como forma de ocupar tus dedos.",
    "👐 Lleva anillos o pulseras que puedas girar o mover suavemente.",
    "👐 Aprieta una esponja o pelota blanda con ritmo cuando te sientas ansioso.",
    "👐 Usa un spinner, cubo antiestrés o similar durante reuniones o llamadas.",
    "👐 Apoya tus manos sobre la mesa de forma consciente en lugar de gesticular al vacío.",
    "👐 Cambia el hábito de sujetar un cigarro por sujetar una bebida sana.",
    "👐 Si fumas al conducir, reemplaza con chicle o coloca aromas relajantes en el coche.",
    "👐 Haz respiraciones profundas con las manos sobre el abdomen para centrarte.",
    "👐 Juega con una cuerda o cordón de tela, pasando entre los dedos como un rosario.",
    "👐 Haz pequeños movimientos rítmicos con los dedos para redirigir energía.",
    "👐 Usa apps de seguimiento donde puedas pulsar cada vez que evitas fumar.",
    "👐 Ráscate la palma o acaricia algo texturizado en lugar de buscar un cigarro.",
    "👐 Mantén una pizarra o tablero cerca para escribir frases motivadoras a mano.",
    "👐 Cambia tu gesto habitual de fumar por sostener un lápiz y escribir una intención."
  ]
  ,
  "Placer-relajacion": [
    "🎵 Regálate un momento de música relajante al terminar tu jornada.",
    "🎵 Sustituye el cigarro después de comer por un paseo suave de 10 minutos.",
    "🎵 Disfruta de una bebida caliente o un dulce saludable como pausa de placer.",
    "🎵 Medita o realiza respiraciones conscientes durante 3 minutos cuando lo necesites.",
    "🎵 Redescubre pequeños placeres sin humo: leer, dibujar, tomar un baño.",
    "🎵 Planea una actividad semanal que te entusiasme, como cine o cocina creativa.",
    "🎵 Crea una playlist para diferentes estados de ánimo (energía, calma, alegría).",
    "🎵 Compra una vela o incienso con un aroma que te relaje al final del día.",
    "🎵 Hazte un masaje facial o de cuello con tus propias manos por 5 minutos.",
    "🎵 Practica escribir tres cosas que disfrutes cada día.",
    "🎵 Mima tu cuerpo: una ducha caliente, crema con olor favorito, ropa cómoda.",
    "🎵 Mastica chicle con sabor agradable después de comer en lugar de fumar.",
    "🎵 Baila sin preocuparte por cómo te ves: libera placer y energía.",
    "🎵 Tómate una siesta corta si estás cansado, en lugar de ir por un cigarro.",
    "🎵 Visualiza un recuerdo feliz o una meta deseada durante un minuto.",
    "🎵 Prepara tu bebida favorita con cariño: haz del proceso un ritual placentero.",
    "🎵 Crea un rincón acogedor en casa con cojines, luz suave y calma.",
    "🎵 Mira un vídeo que te haga reír o te inspire.",
    "🎵 Regálate un paseo por la naturaleza o cerca del mar si puedes.",
    "🎵 Compra un snack saludable que te guste solo para ti.",
    "🎵 Dibuja libremente aunque no sepas: el placer está en el acto.",
    "🎵 Tócate el corazón con la palma y repítete: “Esto es para cuidarme”.",
    "🎵 Haz una lista de cosas pequeñas que te hacen sentir bien sin tabaco.",
    "🎵 Tómate el primer sorbo de café o té de forma consciente, sin distracciones.",
    "🎵 Escribe una carta para ti mismo agradeciéndote cuidarte."
  ],
  "Reduccion estados negativos": [
    "🧘 Cuando te sientas estresado, haz respiraciones profundas durante 2 minutos.",
    "🧘 Escribe tus pensamientos en una libreta en lugar de encender un cigarro.",
    "🧘 Llama a alguien de confianza para desahogarte en momentos difíciles.",
    "🧘 Usa una app de mindfulness o meditación guiada al sentir ansiedad.",
    "🧘 Haz una pausa y estírate si sientes tensión emocional o mental.",
    "🧘 Visualiza un lugar tranquilo que te dé calma y respira como si estuvieras allí.",
    "🧘 Coloca ambas manos en el pecho y repite: “Estoy aquí para mí”.",
    "🧘 Da un paseo sin móvil, prestando atención a lo que ves y oyes.",
    "🧘 Escribe una carta que no vas a enviar solo para soltar emociones.",
    "🧘 Llora si lo necesitas: es una forma natural de liberar tensión.",
    "🧘 Observa tu respiración sin controlarla: simplemente nota el aire entrar y salir.",
    "🧘 Baila tu frustración con una canción enérgica y sin juicio.",
    "🧘 Mírate al espejo y di una frase compasiva, como “Estoy haciendo lo mejor que puedo”.",
    "🧘 Usa una técnica de enraizamiento: nombra 5 cosas que ves, 4 que oyes, 3 que sientes.",
    "🧘 Haz 10 respiraciones cuadradas: inhala 4 seg, retén 4, exhala 4, retén 4.",
    "🧘 Frota tus manos o brazos para darte una sensación de presencia.",
    "🧘 Ve a un lugar seguro y silencioso por unos minutos para regularte.",
    "🧘 Anota un problema y escribe debajo tres cosas que puedes hacer HOY sobre él.",
    "🧘 Descansa un rato con una manta ligera si estás sobreestimulado.",
    "🧘 Mira el cielo o naturaleza por la ventana: el cambio visual te ayuda a salir de la mente.",
    "🧘 Dibuja cómo te sientes con formas o colores, sin palabras.",
    "🧘 Abre una ventana y respira aire nuevo con conciencia.",
    "🧘 Repite una frase tipo: “Esto es difícil, pero puedo atravesarlo sin fumar”.",
    "🧘 Reconoce tus emociones en voz alta: “Ahora estoy sintiendo enojo”, etc.",
    "🧘 Felicítate internamente cada vez que enfrentas un mal momento sin fumar."
  ],

  "Adiccion": [
    "🩺 Registra las horas en que más te apetece fumar para entender tus hábitos.",
    "🩺 Sustituye el cigarro matutino por un vaso de agua y una rutina activa.",
    "🩺 Haz un compromiso público con alguien de confianza para sentirte apoyado.",
    "🩺 Usa recordatorios visuales (notas, pulseras) con frases motivadoras.",
    "🩺 Celebra cada día libre de humo con una pequeña recompensa personal.",
    "🩺 Haz seguimiento de tus días sin fumar con una app o calendario visible.",
    "🩺 Evita ambientes donde sabes que la tentación es más fuerte.",
    "🩺 Cambia tu desayuno si solías fumar después de comer.",
    "🩺 Usa afirmaciones como: “Yo controlo mis decisiones, no la nicotina”.",
    "🩺 Dúchate con agua fría o templada al sentir fuerte deseo.",
    "🩺 Usa tus manos para escribir, amasar, construir o dibujar durante los momentos críticos.",
    "🩺 Ten una bebida con hielo y sabor que te guste a mano para distraerte.",
    "🩺 Habla con otra persona durante 3 minutos si te dan ganas fuertes de fumar.",
    "🩺 Practica respiración cuadrada (4-4-4-4) al despertar con necesidad.",
    "🩺 Revisa cada día tus motivos personales para dejar de fumar.",
    "🩺 Observa tu deseo como una ola que sube… pero luego baja.",
    "🩺 Aprende a decirte “ahora no”, incluso si no es un “nunca más”.",
    "🩺 Cambia el lugar donde te sientas a desayunar o ver la TV.",
    "🩺 Visualiza cómo te sentirás dentro de 1 mes sin fumar.",
    "🩺 Repite una frase tipo: “Esto es difícil, pero es temporal”.",
    "🩺 Come algo crujiente y saludable (zanahoria, manzana) como sustituto oral.",
    "🩺 Haz ejercicio breve al sentir ansiedad por nicotina: 10 flexiones o 1 caminata rápida.",
    "🩺 Lava tus manos o dientes para cambiar el impulso físico.",
    "🩺 Recompénsate semanalmente por mantenerte libre.",
    "🩺 Mantén un plan claro para momentos de crisis: ¡ya sabes qué hacer!"
  ],
  "Automatismo": [
    "🔁 Cambia el orden de tu rutina matinal para romper patrones automáticos.",
    "🔁 Crea nuevas rutas al ir al trabajo para evitar lugares asociados al cigarro.",
    "🔁 Desactiva alarmas que te recuerdan indirectamente a fumar.",
    "🔁 Realiza tus actividades típicas con una pequeña variación diaria.",
    "🔁 Pon un aviso visual en tu escritorio: “¿Realmente lo necesitas o es costumbre?”",
    "🔁 Haz una pausa consciente cuando vayas a realizar algo automáticamente.",
    "🔁 Cambia de mano objetos habituales como el móvil o taza para interrumpir el patrón.",
    "🔁 Usa apps de hábitos para registrar cuándo fumas sin darte cuenta.",
    "🔁 Coloca una nota en la puerta de casa o coche con una frase corta: “Presente ahora”.",
    "🔁 Si fumas al conducir, prepara una playlist especial que reemplace el cigarro.",
    "🔁 Sube escaleras, muévete o haz un gesto corporal al notar el impulso.",
    "🔁 Anota cuándo fumas sin pensarlo y revisa los momentos comunes.",
    "🔁 Practica comer en lugares distintos si fumar venía tras comer.",
    "🔁 Usa pulseras o anillos que puedas girar como distracción saludable.",
    "🔁 Establece pausas estructuradas con otros mini-rituales (agua, estiramiento, respiro).",
    "🔁 Haz tu café o té en otro recipiente para evitar la asociación.",
    "🔁 Asocia el deseo automático con una acción consciente: “ahora respiro”.",
    "🔁 Crea un gesto nuevo para los momentos vacíos, como estirarte o contar 5 cosas.",
    "🔁 Haz una lista de tus hábitos automáticos y marca cuáles puedes cambiar.",
    "🔁 Corta el ciclo: si fumas al aburrirte, ten un “plan B” visual cerca.",
    "🔁 Utiliza objetos que sustituyan el gesto como pajitas, cepillos o bolígrafos.",
    "🔁 Activa una alerta diaria que diga: “¿Has elegido esto o fue automático?”",
    "🔁 Haz cambios en tu entorno físico: redecora el rincón donde fumabas.",
    "🔁 Practica observar tu impulso sin reaccionar: sólo respira.",
    "🔁 Usa tu móvil para grabarte una nota de voz y escúchala cuando surja el gesto."
  ],

  "Moderado": [
    "🌱 Aunque fumes poco, cada reducción cuenta. Estás más cerca de dejarlo del todo.",
    "🌱 Aprovecha tu control para apoyar a otros: hablarlo te refuerza a ti también.",
    "🌱 Haz seguimiento de cuándo fumas y por qué: entenderlo es clave para parar.",
    "🌱 Prueba eliminar un cigarro específico del día durante una semana.",
    "🌱 Cuida tus logros: evita retrocesos innecesarios cuando estás cansado o estresado.",
    "🌱 Cambia el entorno para eliminar estímulos que te hagan fumar sin necesidad.",
    "🌱 Replantea momentos sociales: ¿realmente necesitas fumar ahí?",
    "🌱 Practica ir a eventos sin llevar cigarrillos contigo.",
    "🌱 Recuerda que tu cuerpo se beneficia incluso con una reducción mínima.",
    "🌱 Si ya puedes pasar días sin fumar, ¿qué te lo impide toda la semana?",
    "🌱 Felicítate por cada elección consciente que haces a favor de tu salud.",
    "🌱 Lleva un registro de momentos donde elegiste no fumar.",
    "🌱 Sustituye un cigarro ocasional por un vaso de agua o un snack saludable.",
    "🌱 Imagina cómo sería una semana sin tabaco, y pruébalo.",
    "🌱 Conversa con alguien que haya dejado de fumar: sus historias inspiran.",
    "🌱 Redirige la energía de fumar hacia una actividad creativa o física.",
    "🌱 Si fumas para 'acompañar', busca un nuevo gesto: beber, escribir, estirar.",
    "🌱 Revisa tus motivaciones iniciales para reducir: ¿siguen presentes hoy?",
    "🌱 Planea días libres de humo como un reto personal, no como una obligación.",
    "🌱 Establece una meta gradual: por ejemplo, 'hoy 1 menos que ayer'.",
    "🌱 Cuida tus logros con frases como: “Estoy en control, no necesito más”.",
    "🌱 Haz pequeñas celebraciones cada semana sin retrocesos.",
    "🌱 No minimices tu esfuerzo: fumar poco también es un hábito que se puede superar.",
    "🌱 Refuerza tu identidad: no eres un fumador moderado, eres un exfumador en proceso.",
    "🌱 Aléjate de la autocomplacencia: sigue construyendo la mejor versión de ti."
  ]

  };
  

  ngOnInit() {
    this.generarConsejos();
  }

  generarConsejos() {
    const tipos = this.tipoFumador.split(',').map(t => t.trim());
  
  
    this.consejosSeleccionados = tipos.map(tipoOriginal => {
      const tipo = tipoOriginal
        .trim()
        .normalize('NFD')  // descompone letras con tilde
        .replace(/[\u0300-\u036f]/g, '') // elimina signos diacríticos
        .replace(/\b\w/g, c => c.toUpperCase()); // capitaliza
  
      const consejos = this.bancoConsejos[tipo];
  
      const consejoAleatorio = (consejos || this.bancoConsejos["Moderado"])[
        Math.floor(Math.random() * (consejos?.length || 1))
      ];
  
      return { categoria: tipo, consejo: consejoAleatorio };
    });
  }
  cerrar() {
    const modal = document.querySelector('.modal-overlay') as HTMLElement;
    modal?.remove();
  }
}
