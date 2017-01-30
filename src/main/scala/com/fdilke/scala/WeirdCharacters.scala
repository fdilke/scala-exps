package com.fdilke.scala

import scala.tools.nsc.interpreter._

object WeirdCharacters extends App {

//  val interpreter = new IMain
//  val result = interpreter.parse("val x = 2; println(x)")
//  println(s"result = $result")

//  val engine = new ScriptEngineManager().getEngineByName("scala")
//  val settings = engine.asInstanceOf[scala.tools.nsc.interpreter.IMain].settings
  val engine = new IMain(new scala.tools.nsc.Settings)
  val settings = engine.settings
  settings.embeddedDefaults[WeirdCharacters.type]
  settings.usejavacp.value = true
//  println (engine.put("n", 10))
//  println(engine.put("@", 5))
//  val result = engine.eval("1 to n.asInstanceOf[Int] foreach println")
//  println(s"result = $result")

  def validIdentifier(c: Char) =
    engine.beSilentDuring {
      try {
        engine.reset()
        engine.interpret(s"val $c = 2") != null
      } catch {
        case ex: Exception =>
          false
      }
    }

//  for (c <- "x@$>")
//    println(s"$c: ${validIdentifier(c)}")
  var numChars = 0
  val charsPerLine = 80
  for (i <- 0 to 10000) {
    val c = i.toChar
    if (validIdentifier(c)) {
      print(c)
      numChars += 1
      if (numChars == charsPerLine) {
        numChars = 0
        println
      }
    }
  }

//  for (i <- 0 to 16000) {
//    val c: Char = i.toChar
//  }


  val weirdChars = "π≈Ω∏¬⊆√∜∭‱♘☞"
  val perLine = 40

//  for (c: Char <- weirdChars)
//    println(s"$c = ${c.toInt}")

//  for (i <- 0 to 16000) {
//    if (i % perLine == 0) {
//      print(" = 2\nval ")
//      //      print(s"\n${i/perLine * perLine}-${(i/perLine + 1) * perLine}: ")
//    }
//    println(s"val ${i.toChar} = 2")
//  }

/*
  val ໂ = 2
  val ᒠᒡᒢᒣᒤᒥᒦᒧᒨᒩᒪᒫᒬᒭᒮᒯᒰᒱᒲᒳᒴᒵᒶᒷᒸᒹᒺᒻᒼᒽᒾᒿᓀᓁᓂᓃᓄᓅᓆᓇᓈᓉᓊᓋᓌᓍᓎ = 7
  val ◐◑◒◓◔◕◖◗◘◙◚◛◜◝◞◟◠◡◢◣◤◥◦◧◨◩◪◫◬◭◮◯◰◱◲◳◴◵◶◷ = 2
  val ! = 2
  val ¡¨«­²³´·¸¹»¼½¾¿ = 2
val ØÙÚÛÜÝÞßàáâãäåæçèéêëìíîï = 2
val ðñòóôõö÷øùúûüýþÿĀāĂăĄąĆćĈĉĊċČčĎďĐđĒēĔĕĖė = 2
val ĘęĚěĜĝĞğĠġĢģĤĥĦħĨĩĪīĬĭĮįİıĲĳĴĵĶķĸĹĺĻļĽľĿ = 2
val ŀŁłŃńŅņŇňŉŊŋŌōŎŏŐőŒœŔŕŖŗŘřŚśŜŝŞşŠšŢţŤťŦŧ = 2
val ŨũŪūŬŭŮůŰűŲųŴŵŶŷŸŹźŻżŽžſƀƁƂƃƄƅƆƇƈƉƊƋƌƍƎƏ = 2
val ƐƑƒƓƔƕƖƗƘƙƚƛƜƝƞƟƠơƢƣƤƥƦƧƨƩƪƫƬƭƮƯưƱƲƳƴƵƶƷ = 2
val ƸƹƺƻƼƽƾƿǀǁǂǃǄǅǆǇǈǉǊǋǌǍǎǏǐǑǒǓǔǕǖǗǘǙǚǛǜǝǞǟ = 2
val ͰͱͲͳʹ͵Ͷͷ͸͹ͺͻͼͽ;Ϳ΀΁΂΃΄΅Ά·ΈΉΊ΋Ό΍ΎΏΐΑΒΓΔΕΖΗ = 2
val ΘΙΚΛΜΝΞΟΠΡ΢ΣΤΥΦΧΨΩΪΫάέήίΰαβγδεζηθικλμνξο = 2
val πρςστυφχψωϊϋόύώϏϐϑϒϓϔϕϖϗϘϙϚϛϜϝϞϟϠϡϢϣϤϥϦϧ = 2
val ـفقكلمنهوىيًٌٍَُِّْٕٖٜٟٓٔٗ٘ٙٚٛٝٞ٠١٢٣٤٥٦٧ = 2
val ഠഡഢണതഥദധനഩപഫബഭമയരറലളഴവശഷസഹഺ഻഼ഽാിീുൂൃൄ൅െേ = 2
val ᘈᘉᘊᘋᘌᘍᘎᘏᘐᘑᘒᘓᘔᘕᘖᘗᘘᘙᘚᘛᘜᘝᘞᘟᘠᘡᘢᘣᘤᘥᘦᘧᘨᘩᘪᘫᘬᘭᘮᘯ = 2
val ᘰᘱᘲᘳᘴᘵᘶᘷᘸᘹᘺᘻᘼᘽᘾᘿᙀᙁᙂᙃᙄᙅᙆᙇᙈᙉᙊᙋᙌᙍᙎᙏᙐᙑᙒᙓᙔᙕᙖᙗ = 2
val ᙘᙙᙚᙛᙜᙝᙞᙟᙠᙡᙢᙣᙤᙥᙦᙧᙨᙩᙪᙫᙬ᙭᙮ᙯᙰᙱᙲᙳᙴᙵᙶᙷᙸᙹᙺᙻᙼᙽᙾᙿ = 2
val  ᚁᚂᚃᚄᚅᚆᚇᚈᚉᚊᚋᚌᚍᚎᚏᚐᚑᚒᚓᚔᚕᚖᚗᚘᚙᚚ᚛᚜᚝᚞᚟ᚠᚡᚢᚣᚤᚥᚦᚧ = 2
val ᚨᚩᚪᚫᚬᚭᚮᚯᚰᚱᚲᚳᚴᚵᚶᚷᚸᚹᚺᚻᚼᚽᚾᚿᛀᛁᛂᛃᛄᛅᛆᛇᛈᛉᛊᛋᛌᛍᛎᛏ = 2
val ᛐᛑᛒᛓᛔᛕᛖᛗᛘᛙᛚᛛᛜᛝᛞᛟᛠᛡᛢᛣᛤᛥᛦᛧᛨᛩᛪ᛫᛬᛭ᛮᛯᛰᛱᛲᛳᛴᛵᛶᛷ = 2
val ᛸ᛹᛺᛻᛼᛽᛾᛿ᜀᜁᜂᜃᜄᜅᜆᜇᜈᜉᜊᜋᜌᜍᜎᜏᜐᜑᜒᜓ᜔᜕᜖᜗᜘᜙᜚᜛᜜᜝᜞ᜟ = 2
val ᜠᜡᜢᜣᜤᜥᜦᜧᜨᜩᜪᜫᜬᜭᜮᜯᜰᜱᜲᜳ᜴᜵᜶᜷᜸᜹᜺᜻᜼᜽᜾᜿ᝀᝁᝂᝃᝄᝅᝆᝇ = 2
val ᝈᝉᝊᝋᝌᝍᝎᝏᝐᝑᝒᝓ᝔᝕᝖᝗᝘᝙᝚᝛᝜᝝᝞᝟ᝠᝡᝢᝣᝤᝥᝦᝧᝨᝩᝪᝫᝬ᝭ᝮᝯ = 2
val ᝰ᝱ᝲᝳ᝴᝵᝶᝷᝸᝹᝺᝻᝼᝽᝾᝿កខគឃងចឆជឈញដឋឌឍណតថទធនបផពភ = 2
val មយរលវឝឞសហឡអឣឤឥឦឧឨឩឪឫឬឭឮឯឰឱឲឳ឴឵ាិីឹឺុូួើឿ = 2
val ៀេែៃោៅំះៈ៉៊់៌៍៎៏័៑្៓។៕៖ៗ៘៙៚៛ៜ៝៞៟០១២៣៤៥៦៧ = 2
val ↘↙↚↛↜↝↞↟↠↡↢↣↤↥↦↧↨↩↪↫↬↭↮↯↰↱↲↳↴↵↶↷↸↹↺↻↼↽↾↿ = 2
val ⇀⇁⇂⇃⇄⇅⇆⇇⇈⇉⇊⇋⇌⇍⇎⇏⇐⇑⇒⇓⇔⇕⇖⇗⇘⇙⇚⇛⇜⇝⇞⇟⇠⇡⇢⇣⇤⇥⇦⇧ = 2
val ⇨⇩⇪⇫⇬⇭⇮⇯⇰⇱⇲⇳⇴⇵⇶⇷⇸⇹⇺⇻⇼⇽⇾⇿∀∁∂∃∄∅∆∇∈∉∊∋∌∍∎∏ = 2
val ∐∑−∓∔∕∖∗∘∙√∛∜∝∞∟∠∡∢∣∤∥∦∧∨∩∪∫∬∭∮∯∰∱∲∳∴∵∶∷ = 2
val ∸∹∺∻∼∽∾∿≀≁≂≃≄≅≆≇≈≉≊≋≌≍≎≏≐≑≒≓≔≕≖≗≘≙≚≛≜≝≞≟ = 2
val ≠≡≢≣≤≥≦≧≨≩≪≫≬≭≮≯≰≱≲≳≴≵≶≷≸≹≺≻≼≽≾≿⊀⊁⊂⊃⊄⊅⊆⊇ = 2
val ⊈⊉⊊⊋⊌⊍⊎⊏⊐⊑⊒⊓⊔⊕⊖⊗⊘⊙⊚⊛⊜⊝⊞⊟⊠⊡⊢⊣⊤⊥⊦⊧⊨⊩⊪⊫⊬⊭⊮⊯ = 2
val ⊰⊱⊲⊳⊴⊵⊶⊷⊸⊹⊺⊻⊼⊽⊾⊿⋀⋁⋂⋃⋄⋅⋆⋇⋈⋉⋊⋋⋌⋍⋎⋏⋐⋑⋒⋓⋔⋕⋖⋗ = 2
val ⋘⋙⋚⋛⋜⋝⋞⋟⋠⋡⋢⋣⋤⋥⋦⋧⋨⋩⋪⋫⋬⋭⋮⋯⋰⋱⋲⋳⋴⋵⋶⋷⋸⋹⋺⋻⋼⋽⋾⋿ = 2
val ⌀⌁⌂⌃⌄⌅⌆⌇⌈⌉⌊⌋⌌⌍⌎⌏⌐⌑⌒⌓⌔⌕⌖⌗⌘⌙⌚⌛⌜⌝⌞⌟⌠⌡⌢⌣⌤⌥⌦⌧ = 2
val ⌨〈〉⌫⌬⌭⌮⌯⌰⌱⌲⌳⌴⌵⌶⌷⌸⌹⌺⌻⌼⌽⌾⌿⍀⍁⍂⍃⍄⍅⍆⍇⍈⍉⍊⍋⍌⍍⍎⍏ = 2
val ⍐⍑⍒⍓⍔⍕⍖⍗⍘⍙⍚⍛⍜⍝⍞⍟⍠⍡⍢⍣⍤⍥⍦⍧⍨⍩⍪⍫⍬⍭⍮⍯⍰⍱⍲⍳⍴⍵⍶⍷ = 2
val ⍸⍹⍺⍻⍼⍽⍾⍿⎀⎁⎂⎃⎄⎅⎆⎇⎈⎉⎊⎋⎌⎍⎎⎏⎐⎑⎒⎓⎔⎕⎖⎗⎘⎙⎚⎛⎜⎝⎞⎟ = 2
val ⎠⎡⎢⎣⎤⎥⎦⎧⎨⎩⎪⎫⎬⎭⎮⎯⎰⎱⎲⎳⎴⎵⎶⎷⎸⎹⎺⎻⎼⎽⎾⎿⏀⏁⏂⏃⏄⏅⏆⏇ = 2
val ⏈⏉⏊⏋⏌⏍⏎⏏⏐⏑⏒⏓⏔⏕⏖⏗⏘⏙⏚⏛⏜⏝⏞⏟⏠⏡⏢⏣⏤⏥⏦⏧⏨⏩⏪⏫⏬⏭⏮⏯ = 2
val ⏰⏱⏲⏳⏴⏵⏶⏷⏸⏹⏺⏻⏼⏽⏾⏿␀␁␂␃␄␅␆␇␈␉␊␋␌␍␎␏␐␑␒␓␔␕␖␗ = 2
val ␘␙␚␛␜␝␞␟␠␡␢␣␤␥␦␧␨␩␪␫␬␭␮␯␰␱␲␳␴␵␶␷␸␹␺␻␼␽␾␿ = 2
val ⑀⑁⑂⑃⑄⑅⑆⑇⑈⑉⑊⑋⑌⑍⑎⑏⑐⑑⑒⑓⑔⑕⑖⑗⑘⑙⑚⑛⑜⑝⑞⑟①②③④⑤⑥⑦⑧ = 2
val ⑨⑩⑪⑫⑬⑭⑮⑯⑰⑱⑲⑳⑴⑵⑶⑷⑸⑹⑺⑻⑼⑽⑾⑿⒀⒁⒂⒃⒄⒅⒆⒇⒈⒉⒊⒋⒌⒍⒎⒏ = 2
val ⒐⒑⒒⒓⒔⒕⒖⒗⒘⒙⒚⒛⒜⒝⒞⒟⒠⒡⒢⒣⒤⒥⒦⒧⒨⒩⒪⒫⒬⒭⒮⒯⒰⒱⒲⒳⒴⒵ⒶⒷ = 2
val ⒸⒹⒺⒻⒼⒽⒾⒿⓀⓁⓂⓃⓄⓅⓆⓇⓈⓉⓊⓋⓌⓍⓎⓏⓐⓑⓒⓓⓔⓕⓖⓗⓘⓙⓚⓛⓜⓝⓞⓟ = 2
val ⓠⓡⓢⓣⓤⓥⓦⓧⓨⓩ⓪⓫⓬⓭⓮⓯⓰⓱⓲⓳⓴⓵⓶⓷⓸⓹⓺⓻⓼⓽⓾⓿─━│┃┄┅┆┇ = 2
val ┈┉┊┋┌┍┎┏┐┑┒┓└┕┖┗┘┙┚┛├┝┞┟┠┡┢┣┤┥┦┧┨┩┪┫┬┭┮┯ = 2
val ┰┱┲┳┴┵┶┷┸┹┺┻┼┽┾┿╀╁╂╃╄╅╆╇╈╉╊╋╌╍╎╏═║╒╓╔╕╖╗ = 2
val ╘╙╚╛╜╝╞╟╠╡╢╣╤╥╦╧╨╩╪╫╬╭╮╯╰╱╲╳╴╵╶╷╸╹╺╻╼╽╾╿ = 2
val ▀▁▂▃▄▅▆▇█▉▊▋▌▍▎▏▐░▒▓▔▕▖▗▘▙▚▛▜▝▞▟■□▢▣▤▥▦▧ = 2
val ▨▩▪▫▬▭▮▯▰▱▲△▴▵▶▷▸▹►▻▼▽▾▿◀◁◂◃◄◅◆◇◈◉◊○◌◍◎● = 2
val ◐◑◒◓◔◕◖◗◘◙◚◛◜◝◞◟◠◡◢◣◤◥◦◧◨◩◪◫◬◭◮◯◰◱◲◳◴◵◶◷ = 2
val ◸◹◺◻◼◽◾◿☀☁☂☃☄★☆☇☈☉☊☋☌☍☎☏☐☑☒☓☔☕☖☗☘☙☚☛☜☝☞☟ = 2
val ☠☡☢☣☤☥☦☧☨☩☪☫☬☭☮☯☰☱☲☳☴☵☶☷☸☹☺☻☼☽☾☿♀♁♂♃♄♅♆♇ = 2
val ♈♉♊♋♌♍♎♏♐♑♒♓♔♕♖♗♘♙♚♛♜♝♞♟♠♡♢♣♤♥♦♧♨♩♪♫♬♭♮♯ = 2
val ♰♱♲♳♴♵♶♷♸♹♺♻♼♽♾♿⚀⚁⚂⚃⚄⚅⚆⚇⚈⚉⚊⚋⚌⚍⚎⚏⚐⚑⚒⚓⚔⚕⚖⚗ = 2
val ⚘⚙⚚⚛⚜⚝⚞⚟⚠⚡⚢⚣⚤⚥⚦⚧⚨⚩⚪⚫⚬⚭⚮⚯⚰⚱⚲⚳⚴⚵⚶⚷⚸⚹⚺⚻⚼⚽⚾⚿ = 2
val ⛀⛁⛂⛃⛄⛅⛆⛇⛈⛉⛊⛋⛌⛍⛎⛏⛐⛑⛒⛓⛔⛕⛖⛗⛘⛙⛚⛛⛜⛝⛞⛟⛠⛡⛢⛣⛤⛥⛦⛧ = 2
val ⛨⛩⛪⛫⛬⛭⛮⛯⛰⛱⛲⛳⛴⛵⛶⛷⛸⛹⛺⛻⛼⛽⛾⛿✀✁✂✃✄✅✆✇✈✉✊✋✌✍✎✏ = 2
val ✐✑✒✓✔✕✖✗✘✙✚✛✜✝✞✟✠✡✢✣✤✥✦✧✨✩✪✫✬✭✮✯✰✱✲✳✴✵✶✷ = 2
val ✸✹✺✻✼✽✾✿❀❁❂❃❄❅❆❇❈❉❊❋❌❍❎❏❐❑❒❓❔❕❖❗❘❙❚❛❜❝❞❟ = 2
val ❠❡❢❣❤❥❦❧❨❩❪❫❬❭❮❯❰❱❲❳❴❵❶❷❸❹❺❻❼❽❾❿➀➁➂➃➄➅➆➇ = 2
val ➈➉➊➋➌➍➎➏➐➑➒➓➔➕➖➗➘➙➚➛➜➝➞➟➠➡➢➣➤➥➦➧➨➩➪➫➬➭➮➯ = 2
val ➰➱➲➳➴➵➶➷➸➹➺➻➼➽➾➿⟀⟁⟂⟃⟄⟅⟆⟇⟈⟉⟊⟋⟌⟍⟎⟏⟐⟑⟒⟓⟔⟕⟖⟗ = 2
val ⟘⟙⟚⟛⟜⟝⟞⟟⟠⟡⟢⟣⟤⟥⟦⟧⟨⟩⟪⟫⟬⟭⟮⟯⟰⟱⟲⟳⟴⟵⟶⟷⟸⟹⟺⟻⟼⟽⟾⟿ = 2
val ⠀⠁⠂⠃⠄⠅⠆⠇⠈⠉⠊⠋⠌⠍⠎⠏⠐⠑⠒⠓⠔⠕⠖⠗⠘⠙⠚⠛⠜⠝⠞⠟⠠⠡⠢⠣⠤⠥⠦⠧ = 2
val ⠨⠩⠪⠫⠬⠭⠮⠯⠰⠱⠲⠳⠴⠵⠶⠷⠸⠹⠺⠻⠼⠽⠾⠿⡀⡁⡂⡃⡄⡅⡆⡇⡈⡉⡊⡋⡌⡍⡎⡏ = 2
val ⡐⡑⡒⡓⡔⡕⡖⡗⡘⡙⡚⡛⡜⡝⡞⡟⡠⡡⡢⡣⡤⡥⡦⡧⡨⡩⡪⡫⡬⡭⡮⡯⡰⡱⡲⡳⡴⡵⡶⡷ = 2
val ⡸⡹⡺⡻⡼⡽⡾⡿⢀⢁⢂⢃⢄⢅⢆⢇⢈⢉⢊⢋⢌⢍⢎⢏⢐⢑⢒⢓⢔⢕⢖⢗⢘⢙⢚⢛⢜⢝⢞⢟ = 2
val ⢠⢡⢢⢣⢤⢥⢦⢧⢨⢩⢪⢫⢬⢭⢮⢯⢰⢱⢲⢳⢴⢵⢶⢷⢸⢹⢺⢻⢼⢽⢾⢿⣀⣁⣂⣃⣄⣅⣆⣇ = 2
val ⣈⣉⣊⣋⣌⣍⣎⣏⣐⣑⣒⣓⣔⣕⣖⣗⣘⣙⣚⣛⣜⣝⣞⣟⣠⣡⣢⣣⣤⣥⣦⣧⣨⣩⣪⣫⣬⣭⣮⣯ = 2
val ⣰⣱⣲⣳⣴⣵⣶⣷⣸⣹⣺⣻⣼⣽⣾⣿⤀⤁⤂⤃⤄⤅⤆⤇⤈⤉⤊⤋⤌⤍⤎⤏⤐⤑⤒⤓⤔⤕⤖⤗ = 2
val ⤘⤙⤚⤛⤜⤝⤞⤟⤠⤡⤢⤣⤤⤥⤦⤧⤨⤩⤪⤫⤬⤭⤮⤯⤰⤱⤲⤳⤴⤵⤶⤷⤸⤹⤺⤻⤼⤽⤾⤿ = 2
val ⥀⥁⥂⥃⥄⥅⥆⥇⥈⥉⥊⥋⥌⥍⥎⥏⥐⥑⥒⥓⥔⥕⥖⥗⥘⥙⥚⥛⥜⥝⥞⥟⥠⥡⥢⥣⥤⥥⥦⥧ = 2
val ⥨⥩⥪⥫⥬⥭⥮⥯⥰⥱⥲⥳⥴⥵⥶⥷⥸⥹⥺⥻⥼⥽⥾⥿⦀⦁⦂⦃⦄⦅⦆⦇⦈⦉⦊⦋⦌⦍⦎⦏ = 2
val ⦐⦑⦒⦓⦔⦕⦖⦗⦘⦙⦚⦛⦜⦝⦞⦟⦠⦡⦢⦣⦤⦥⦦⦧⦨⦩⦪⦫⦬⦭⦮⦯⦰⦱⦲⦳⦴⦵⦶⦷ = 2
val ⦸⦹⦺⦻⦼⦽⦾⦿⧀⧁⧂⧃⧄⧅⧆⧇⧈⧉⧊⧋⧌⧍⧎⧏⧐⧑⧒⧓⧔⧕⧖⧗⧘⧙⧚⧛⧜⧝⧞⧟ = 2
val ⧠⧡⧢⧣⧤⧥⧦⧧⧨⧩⧪⧫⧬⧭⧮⧯⧰⧱⧲⧳⧴⧵⧶⧷⧸⧹⧺⧻⧼⧽⧾⧿⨀⨁⨂⨃⨄⨅⨆⨇ = 2
val ⨈⨉⨊⨋⨌⨍⨎⨏⨐⨑⨒⨓⨔⨕⨖⨗⨘⨙⨚⨛⨜⨝⨞⨟⨠⨡⨢⨣⨤⨥⨦⨧⨨⨩⨪⨫⨬⨭⨮⨯ = 2
val ⨰⨱⨲⨳⨴⨵⨶⨷⨸⨹⨺⨻⨼⨽⨾⨿⩀⩁⩂⩃⩄⩅⩆⩇⩈⩉⩊⩋⩌⩍⩎⩏⩐⩑⩒⩓⩔⩕⩖⩗ = 2
val ⩘⩙⩚⩛⩜⩝⩞⩟⩠⩡⩢⩣⩤⩥⩦⩧⩨⩩⩪⩫⩬⩭⩮⩯⩰⩱⩲⩳⩴⩵⩶⩷⩸⩹⩺⩻⩼⩽⩾⩿ = 2
val ⪀⪁⪂⪃⪄⪅⪆⪇⪈⪉⪊⪋⪌⪍⪎⪏⪐⪑⪒⪓⪔⪕⪖⪗⪘⪙⪚⪛⪜⪝⪞⪟⪠⪡⪢⪣⪤⪥⪦⪧ = 2
val ⪨⪩⪪⪫⪬⪭⪮⪯⪰⪱⪲⪳⪴⪵⪶⪷⪸⪹⪺⪻⪼⪽⪾⪿⫀⫁⫂⫃⫄⫅⫆⫇⫈⫉⫊⫋⫌⫍⫎⫏ = 2
val ⫐⫑⫒⫓⫔⫕⫖⫗⫘⫙⫚⫛⫝̸⫝⫞⫟⫠⫡⫢⫣⫤⫥⫦⫧⫨⫩⫪⫫⫬⫭⫮⫯⫰⫱⫲⫳⫴⫵⫶⫷ = 2
val ⫸⫹⫺⫻⫼⫽⫾⫿⬀⬁⬂⬃⬄⬅⬆⬇⬈⬉⬊⬋⬌⬍⬎⬏⬐⬑⬒⬓⬔⬕⬖⬗⬘⬙⬚⬛⬜⬝⬞⬟ = 2
val ⬠⬡⬢⬣⬤⬥⬦⬧⬨⬩⬪⬫⬬⬭⬮⬯⬰⬱⬲⬳⬴⬵⬶⬷⬸⬹⬺⬻⬼⬽⬾⬿⭀⭁⭂⭃⭄⭅⭆⭇ = 2
val ⭈⭉⭊⭋⭌⭍⭎⭏⭐⭑⭒⭓⭔⭕⭖⭗⭘⭙⭚⭛⭜⭝⭞⭟⭠⭡⭢⭣⭤⭥⭦⭧⭨⭩⭪⭫⭬⭭⭮⭯ = 2
val ⭰⭱⭲⭳⭴⭵⭶⭷⭸⭹⭺⭻⭼⭽⭾⭿⮀⮁⮂⮃⮄⮅⮆⮇⮈⮉⮊⮋⮌⮍⮎⮏⮐⮑⮒⮓⮔⮕⮖⮗ = 2
val ⮘⮙⮚⮛⮜⮝⮞⮟⮠⮡⮢⮣⮤⮥⮦⮧⮨⮩⮪⮫⮬⮭⮮⮯⮰⮱⮲⮳⮴⮵⮶⮷⮸⮹⮺⮻⮼⮽⮾⮿ = 2
val ⯀⯁⯂⯃⯄⯅⯆⯇⯈⯉⯊⯋⯌⯍⯎⯏⯐⯑⯒⯓⯔⯕⯖⯗⯘⯙⯚⯛⯜⯝⯞⯟⯠⯡⯢⯣⯤⯥⯦⯧ = 2
val ⯨⯩⯪⯫⯬⯭⯮⯯⯰⯱⯲⯳⯴⯵⯶⯷⯸⯹⯺⯻⯼⯽⯾⯿ⰀⰁⰂⰃⰄⰅⰆⰇⰈⰉⰊⰋⰌⰍⰎⰏ = 2
val ⰐⰑⰒⰓⰔⰕⰖⰗⰘⰙⰚⰛⰜⰝⰞⰟⰠⰡⰢⰣⰤⰥⰦⰧⰨⰩⰪⰫⰬⰭⰮⰯⰰⰱⰲⰳⰴⰵⰶⰷ = 2
val ⰸⰹⰺⰻⰼⰽⰾⰿⱀⱁⱂⱃⱄⱅⱆⱇⱈⱉⱊⱋⱌⱍⱎⱏⱐⱑⱒⱓⱔⱕⱖⱗⱘⱙⱚⱛⱜⱝⱞⱟ = 2
val ⱠⱡⱢⱣⱤⱥⱦⱧⱨⱩⱪⱫⱬⱭⱮⱯⱰⱱⱲⱳⱴⱵⱶⱷⱸⱹⱺⱻⱼⱽⱾⱿⲀⲁⲂⲃⲄⲅⲆⲇ = 2
val ⲈⲉⲊⲋⲌⲍⲎⲏⲐⲑⲒⲓⲔⲕⲖⲗⲘⲙⲚⲛⲜⲝⲞⲟⲠⲡⲢⲣⲤⲥⲦⲧⲨⲩⲪⲫⲬⲭⲮⲯ = 2
val ⲰⲱⲲⲳⲴⲵⲶⲷⲸⲹⲺⲻⲼⲽⲾⲿⳀⳁⳂⳃⳄⳅⳆⳇⳈⳉⳊⳋⳌⳍⳎⳏⳐⳑⳒⳓⳔⳕⳖⳗ = 2
val ⳘⳙⳚⳛⳜⳝⳞⳟⳠⳡⳢⳣⳤ⳥⳦⳧⳨⳩⳪ⳫⳬⳭⳮ⳯⳰⳱Ⳳⳳ⳴⳵⳶⳷⳸⳹⳺⳻⳼⳽⳾⳿ = 2
val ⴀⴁⴂⴃⴄⴅⴆⴇⴈⴉⴊⴋⴌⴍⴎⴏⴐⴑⴒⴓⴔⴕⴖⴗⴘⴙⴚⴛⴜⴝⴞⴟⴠⴡⴢⴣⴤⴥ⴦ⴧ = 2
val ⴨⴩⴪⴫⴬ⴭ⴮⴯ⴰⴱⴲⴳⴴⴵⴶⴷⴸⴹⴺⴻⴼⴽⴾⴿⵀⵁⵂⵃⵄⵅⵆⵇⵈⵉⵊⵋⵌⵍⵎⵏ = 2
val ⵐⵑⵒⵓⵔⵕⵖⵗⵘⵙⵚⵛⵜⵝⵞⵟⵠⵡⵢⵣⵤⵥⵦⵧ⵨⵩⵪⵫⵬⵭⵮ⵯ⵰⵱⵲⵳⵴⵵⵶⵷ = 2
val ⵸⵹⵺⵻⵼⵽⵾⵿ⶀⶁⶂⶃⶄⶅⶆⶇⶈⶉⶊⶋⶌⶍⶎⶏⶐⶑⶒⶓⶔⶕⶖ⶗⶘⶙⶚⶛⶜⶝⶞⶟ = 2
val ⶠⶡⶢⶣⶤⶥⶦ⶧ⶨⶩⶪⶫⶬⶭⶮ⶯ⶰⶱⶲⶳⶴⶵⶶ⶷ⶸⶹⶺⶻⶼⶽⶾ⶿ⷀⷁⷂⷃⷄⷅⷆ⷇ = 2
val ⷈⷉⷊⷋⷌⷍⷎ⷏ⷐⷑⷒⷓⷔⷕⷖ⷗ⷘⷙⷚⷛⷜⷝⷞ⷟ⷠⷡⷢⷣⷤⷥⷦⷧⷨⷩⷪⷫⷬⷭⷮⷯ = 2
val ⷰⷱⷲⷳⷴⷵⷶⷷⷸⷹⷺⷻⷼⷽⷾⷿ⸀⸁⸂⸃⸄⸅⸆⸇⸈⸉⸊⸋⸌⸍⸎⸏⸐⸑⸒⸓⸔⸕⸖⸗ = 2
val ⸘⸙⸚⸛⸜⸝⸞⸟⸠⸡⸢⸣⸤⸥⸦⸧⸨⸩⸪⸫⸬⸭⸮ⸯ⸰⸱⸲⸳⸴⸵⸶⸷⸸⸹⸺⸻⸼⸽⸾⸿ = 2
val ⹀⹁⹂⹃⹄⹅⹆⹇⹈⹉⹊⹋⹌⹍⹎⹏⹐⹑⹒⹓⹔⹕⹖⹗⹘⹙⹚⹛⹜⹝⹞⹟⹠⹡⹢⹣⹤⹥⹦⹧ = 2
val ⹨⹩⹪⹫⹬⹭⹮⹯⹰⹱⹲⹳⹴⹵⹶⹷⹸⹹⹺⹻⹼⹽⹾⹿⺀⺁⺂⺃⺄⺅⺆⺇⺈⺉⺊⺋⺌⺍⺎⺏ = 2
val ⺐⺑⺒⺓⺔⺕⺖⺗⺘⺙⺚⺛⺜⺝⺞⺟⺠⺡⺢⺣⺤⺥⺦⺧⺨⺩⺪⺫⺬⺭⺮⺯⺰⺱⺲⺳⺴⺵⺶⺷ = 2
val ⺸⺹⺺⺻⺼⺽⺾⺿⻀⻁⻂⻃⻄⻅⻆⻇⻈⻉⻊⻋⻌⻍⻎⻏⻐⻑⻒⻓⻔⻕⻖⻗⻘⻙⻚⻛⻜⻝⻞⻟ = 2
val ⻠⻡⻢⻣⻤⻥⻦⻧⻨⻩⻪⻫⻬⻭⻮⻯⻰⻱⻲⻳⻴⻵⻶⻷⻸⻹⻺⻻⻼⻽⻾⻿⼀⼁⼂⼃⼄⼅⼆⼇ = 2
val ⼈⼉⼊⼋⼌⼍⼎⼏⼐⼑⼒⼓⼔⼕⼖⼗⼘⼙⼚⼛⼜⼝⼞⼟⼠⼡⼢⼣⼤⼥⼦⼧⼨⼩⼪⼫⼬⼭⼮⼯ = 2
val ⼰⼱⼲⼳⼴⼵⼶⼷⼸⼹⼺⼻⼼⼽⼾⼿⽀⽁⽂⽃⽄⽅⽆⽇⽈⽉⽊⽋⽌⽍⽎⽏⽐⽑⽒⽓⽔⽕⽖⽗ = 2
val ⽘⽙⽚⽛⽜⽝⽞⽟⽠⽡⽢⽣⽤⽥⽦⽧⽨⽩⽪⽫⽬⽭⽮⽯⽰⽱⽲⽳⽴⽵⽶⽷⽸⽹⽺⽻⽼⽽⽾⽿ = 2
val ⾀⾁⾂⾃⾄⾅⾆⾇⾈⾉⾊⾋⾌⾍⾎⾏⾐⾑⾒⾓⾔⾕⾖⾗⾘⾙⾚⾛⾜⾝⾞⾟⾠⾡⾢⾣⾤⾥⾦⾧ = 2
val ⾨⾩⾪⾫⾬⾭⾮⾯⾰⾱⾲⾳⾴⾵⾶⾷⾸⾹⾺⾻⾼⾽⾾⾿⿀⿁⿂⿃⿄⿅⿆⿇⿈⿉⿊⿋⿌⿍⿎⿏ = 2
val ⿐⿑⿒⿓⿔⿕⿖⿗⿘⿙⿚⿛⿜⿝⿞⿟⿠⿡⿢⿣⿤⿥⿦⿧⿨⿩⿪⿫⿬⿭⿮⿯⿰⿱⿲⿳⿴⿵⿶⿷ = 2
val ⿸⿹⿺⿻⿼⿽⿾⿿　、。〃〄々〆〇〈〉《》「」『』【】〒〓〔〕〖〗〘〙〚〛〜〝〞〟 = 2
val 〠〡〢〣〤〥〦〧〨〩〪〭〮〯〫〬〰〱〲〳〴〵〶〷〸〹〺〻〼〽〾〿぀ぁあぃいぅうぇ = 2
val えぉおかがきぎくぐけげこごさざしじすずせぜそぞただちぢっつづてでとどなにぬねのは = 2
val ばぱひびぴふぶぷへべぺほぼぽまみむめもゃやゅゆょよらりるれろゎわゐゑをんゔゕゖ゗ = 2
val ゘゙゚゛゜ゝゞゟ゠ァアィイゥウェエォオカガキギクグケゲコゴサザシジスズセゼソゾタ = 2
val ダチヂッツヅテデトドナニヌネノハバパヒビピフブプヘベペホボポマミムメモャヤュユョ = 2
val ヨラリルレロヮワヰヱヲンヴヵヶヷヸヹヺ・ーヽヾヿ㄀㄁㄂㄃㄄ㄅㄆㄇㄈㄉㄊㄋㄌㄍㄎㄏ = 2
val ㄐㄑㄒㄓㄔㄕㄖㄗㄘㄙㄚㄛㄜㄝㄞㄟㄠㄡㄢㄣㄤㄥㄦㄧㄨㄩㄪㄫㄬㄭㄮㄯ㄰ㄱㄲㄳㄴㄵㄶㄷ = 2
val ㄸㄹㄺㄻㄼㄽㄾㄿㅀㅁㅂㅃㅄㅅㅆㅇㅈㅉㅊㅋㅌㅍㅎㅏㅐㅑㅒㅓㅔㅕㅖㅗㅘㅙㅚㅛㅜㅝㅞㅟ = 2
val ㅠㅡㅢㅣㅤㅥㅦㅧㅨㅩㅪㅫㅬㅭㅮㅯㅰㅱㅲㅳㅴㅵㅶㅷㅸㅹㅺㅻㅼㅽㅾㅿㆀㆁㆂㆃㆄㆅㆆㆇ = 2
val ㆈㆉㆊㆋㆌㆍㆎ㆏㆐㆑㆒㆓㆔㆕㆖㆗㆘㆙㆚㆛㆜㆝㆞㆟ㆠㆡㆢㆣㆤㆥㆦㆧㆨㆩㆪㆫㆬㆭㆮㆯ = 2
val ㆰㆱㆲㆳㆴㆵㆶㆷㆸㆹㆺㆻㆼㆽㆾㆿ㇀㇁㇂㇃㇄㇅㇆㇇㇈㇉㇊㇋㇌㇍㇎㇏㇐㇑㇒㇓㇔㇕㇖㇗ = 2
val ㇘㇙㇚㇛㇜㇝㇞㇟㇠㇡㇢㇣㇤㇥㇦㇧㇨㇩㇪㇫㇬㇭㇮㇯ㇰㇱㇲㇳㇴㇵㇶㇷㇸㇹㇺㇻㇼㇽㇾㇿ = 2
val ㈀㈁㈂㈃㈄㈅㈆㈇㈈㈉㈊㈋㈌㈍㈎㈏㈐㈑㈒㈓㈔㈕㈖㈗㈘㈙㈚㈛㈜㈝㈞㈟㈠㈡㈢㈣㈤㈥㈦㈧ = 2
val ㈨㈩㈪㈫㈬㈭㈮㈯㈰㈱㈲㈳㈴㈵㈶㈷㈸㈹㈺㈻㈼㈽㈾㈿㉀㉁㉂㉃㉄㉅㉆㉇㉈㉉㉊㉋㉌㉍㉎㉏ = 2
val ㉐㉑㉒㉓㉔㉕㉖㉗㉘㉙㉚㉛㉜㉝㉞㉟㉠㉡㉢㉣㉤㉥㉦㉧㉨㉩㉪㉫㉬㉭㉮㉯㉰㉱㉲㉳㉴㉵㉶㉷ = 2
val ㉸㉹㉺㉻㉼㉽㉾㉿㊀㊁㊂㊃㊄㊅㊆㊇㊈㊉㊊㊋㊌㊍㊎㊏㊐㊑㊒㊓㊔㊕㊖㊗㊘㊙㊚㊛㊜㊝㊞㊟ = 2
val ㊠㊡㊢㊣㊤㊥㊦㊧㊨㊩㊪㊫㊬㊭㊮㊯㊰㊱㊲㊳㊴㊵㊶㊷㊸㊹㊺㊻㊼㊽㊾㊿㋀㋁㋂㋃㋄㋅㋆㋇ = 2
val ㋈㋉㋊㋋㋌㋍㋎㋏㋐㋑㋒㋓㋔㋕㋖㋗㋘㋙㋚㋛㋜㋝㋞㋟㋠㋡㋢㋣㋤㋥㋦㋧㋨㋩㋪㋫㋬㋭㋮㋯ = 2
val ㋰㋱㋲㋳㋴㋵㋶㋷㋸㋹㋺㋻㋼㋽㋾㋿㌀㌁㌂㌃㌄㌅㌆㌇㌈㌉㌊㌋㌌㌍㌎㌏㌐㌑㌒㌓㌔㌕㌖㌗ = 2
val ㌘㌙㌚㌛㌜㌝㌞㌟㌠㌡㌢㌣㌤㌥㌦㌧㌨㌩㌪㌫㌬㌭㌮㌯㌰㌱㌲㌳㌴㌵㌶㌷㌸㌹㌺㌻㌼㌽㌾㌿ = 2
val ㍀㍁㍂㍃㍄㍅㍆㍇㍈㍉㍊㍋㍌㍍㍎㍏㍐㍑㍒㍓㍔㍕㍖㍗㍘㍙㍚㍛㍜㍝㍞㍟㍠㍡㍢㍣㍤㍥㍦㍧ = 2
val ㍨㍩㍪㍫㍬㍭㍮㍯㍰㍱㍲㍳㍴㍵㍶㍷㍸㍹㍺㍻㍼㍽㍾㍿㎀㎁㎂㎃㎄㎅㎆㎇㎈㎉㎊㎋㎌㎍㎎㎏ = 2
val ㎐㎑㎒㎓㎔㎕㎖㎗㎘㎙㎚㎛㎜㎝㎞㎟㎠㎡㎢㎣㎤㎥㎦㎧㎨㎩㎪㎫㎬㎭㎮㎯㎰㎱㎲㎳㎴㎵㎶㎷ = 2
val ㎸㎹㎺㎻㎼㎽㎾㎿㏀㏁㏂㏃㏄㏅㏆㏇㏈㏉㏊㏋㏌㏍㏎㏏㏐㏑㏒㏓㏔㏕㏖㏗㏘㏙㏚㏛㏜㏝㏞㏟ = 2
val ㏠㏡㏢㏣㏤㏥㏦㏧㏨㏩㏪㏫㏬㏭㏮㏯㏰㏱㏲㏳㏴㏵㏶㏷㏸㏹㏺㏻㏼㏽㏾㏿㐀㐁㐂㐃㐄㐅㐆㐇 = 2
val 㐈㐉㐊㐋㐌㐍㐎㐏㐐㐑㐒㐓㐔㐕㐖㐗㐘㐙㐚㐛㐜㐝㐞㐟㐠㐡㐢㐣㐤㐥㐦㐧㐨㐩㐪㐫㐬㐭㐮㐯 = 2
val 㐰㐱㐲㐳㐴㐵㐶㐷㐸㐹㐺㐻㐼㐽㐾㐿㑀㑁㑂㑃㑄㑅㑆㑇㑈㑉㑊㑋㑌㑍㑎㑏㑐㑑㑒㑓㑔㑕㑖㑗 = 2
val 㑘㑙㑚㑛㑜㑝㑞㑟㑠㑡㑢㑣㑤㑥㑦㑧㑨㑩㑪㑫㑬㑭㑮㑯㑰㑱㑲㑳㑴㑵㑶㑷㑸㑹㑺㑻㑼㑽㑾㑿 = 2
val 㒀㒁㒂㒃㒄㒅㒆㒇㒈㒉㒊㒋㒌㒍㒎㒏㒐㒑㒒㒓㒔㒕㒖㒗㒘㒙㒚㒛㒜㒝㒞㒟㒠㒡㒢㒣㒤㒥㒦㒧 = 2
val 㒨㒩㒪㒫㒬㒭㒮㒯㒰㒱㒲㒳㒴㒵㒶㒷㒸㒹㒺㒻㒼㒽㒾㒿㓀㓁㓂㓃㓄㓅㓆㓇㓈㓉㓊㓋㓌㓍㓎㓏 = 2
val 㓐㓑㓒㓓㓔㓕㓖㓗㓘㓙㓚㓛㓜㓝㓞㓟㓠㓡㓢㓣㓤㓥㓦㓧㓨㓩㓪㓫㓬㓭㓮㓯㓰㓱㓲㓳㓴㓵㓶㓷 = 2
val 㓸㓹㓺㓻㓼㓽㓾㓿㔀㔁㔂㔃㔄㔅㔆㔇㔈㔉㔊㔋㔌㔍㔎㔏㔐㔑㔒㔓㔔㔕㔖㔗㔘㔙㔚㔛㔜㔝㔞㔟 = 2
val 㔠㔡㔢㔣㔤㔥㔦㔧㔨㔩㔪㔫㔬㔭㔮㔯㔰㔱㔲㔳㔴㔵㔶㔷㔸㔹㔺㔻㔼㔽㔾㔿㕀㕁㕂㕃㕄㕅㕆㕇 = 2
val 㕈㕉㕊㕋㕌㕍㕎㕏㕐㕑㕒㕓㕔㕕㕖㕗㕘㕙㕚㕛㕜㕝㕞㕟㕠㕡㕢㕣㕤㕥㕦㕧㕨㕩㕪㕫㕬㕭㕮㕯 = 2
val 㕰㕱㕲㕳㕴㕵㕶㕷㕸㕹㕺㕻㕼㕽㕾㕿㖀㖁㖂㖃㖄㖅㖆㖇㖈㖉㖊㖋㖌㖍㖎㖏㖐㖑㖒㖓㖔㖕㖖㖗 = 2
val 㖘㖙㖚㖛㖜㖝㖞㖟㖠㖡㖢㖣㖤㖥㖦㖧㖨㖩㖪㖫㖬㖭㖮㖯㖰㖱㖲㖳㖴㖵㖶㖷㖸㖹㖺㖻㖼㖽㖾㖿 = 2
val 㗀㗁㗂㗃㗄㗅㗆㗇㗈㗉㗊㗋㗌㗍㗎㗏㗐㗑㗒㗓㗔㗕㗖㗗㗘㗙㗚㗛㗜㗝㗞㗟㗠㗡㗢㗣㗤㗥㗦㗧 = 2
val 㗨㗩㗪㗫㗬㗭㗮㗯㗰㗱㗲㗳㗴㗵㗶㗷㗸㗹㗺㗻㗼㗽㗾㗿㘀㘁㘂㘃㘄㘅㘆㘇㘈㘉㘊㘋㘌㘍㘎㘏 = 2
val 㘐㘑㘒㘓㘔㘕㘖㘗㘘㘙㘚㘛㘜㘝㘞㘟㘠㘡㘢㘣㘤㘥㘦㘧㘨㘩㘪㘫㘬㘭㘮㘯㘰㘱㘲㘳㘴㘵㘶㘷 = 2
val 㘸㘹㘺㘻㘼㘽㘾㘿㙀㙁㙂㙃㙄㙅㙆㙇㙈㙉㙊㙋㙌㙍㙎㙏㙐㙑㙒㙓㙔㙕㙖㙗㙘㙙㙚㙛㙜㙝㙞㙟 = 2
val 㙠㙡㙢㙣㙤㙥㙦㙧㙨㙩㙪㙫㙬㙭㙮㙯㙰㙱㙲㙳㙴㙵㙶㙷㙸㙹㙺㙻㙼㙽㙾㙿㚀㚁㚂㚃㚄㚅㚆㚇 = 2
val 㚈㚉㚊㚋㚌㚍㚎㚏㚐㚑㚒㚓㚔㚕㚖㚗㚘㚙㚚㚛㚜㚝㚞㚟㚠㚡㚢㚣㚤㚥㚦㚧㚨㚩㚪㚫㚬㚭㚮㚯 = 2
val 㚰㚱㚲㚳㚴㚵㚶㚷㚸㚹㚺㚻㚼㚽㚾㚿㛀㛁㛂㛃㛄㛅㛆㛇㛈㛉㛊㛋㛌㛍㛎㛏㛐㛑㛒㛓㛔㛕㛖㛗 = 2
val 㛘㛙㛚㛛㛜㛝㛞㛟㛠㛡㛢㛣㛤㛥㛦㛧㛨㛩㛪㛫㛬㛭㛮㛯㛰㛱㛲㛳㛴㛵㛶㛷㛸㛹㛺㛻㛼㛽㛾㛿 = 2
val 㜀㜁㜂㜃㜄㜅㜆㜇㜈㜉㜊㜋㜌㜍㜎㜏㜐㜑㜒㜓㜔㜕㜖㜗㜘㜙㜚㜛㜜㜝㜞㜟㜠㜡㜢㜣㜤㜥㜦㜧 = 2
val 㜨㜩㜪㜫㜬㜭㜮㜯㜰㜱㜲㜳㜴㜵㜶㜷㜸㜹㜺㜻㜼㜽㜾㜿㝀㝁㝂㝃㝄㝅㝆㝇㝈㝉㝊㝋㝌㝍㝎㝏 = 2
val 㝐㝑㝒㝓㝔㝕㝖㝗㝘㝙㝚㝛㝜㝝㝞㝟㝠㝡㝢㝣㝤㝥㝦㝧㝨㝩㝪㝫㝬㝭㝮㝯㝰㝱㝲㝳㝴㝵㝶㝷 = 2
val 㝸㝹㝺㝻㝼㝽㝾㝿㞀㞁㞂㞃㞄㞅㞆㞇㞈㞉㞊㞋㞌㞍㞎㞏㞐㞑㞒㞓㞔㞕㞖㞗㞘㞙㞚㞛㞜㞝㞞㞟 = 2
val 㞠㞡㞢㞣㞤㞥㞦㞧㞨㞩㞪㞫㞬㞭㞮㞯㞰㞱㞲㞳㞴㞵㞶㞷㞸㞹㞺㞻㞼㞽㞾㞿㟀㟁㟂㟃㟄㟅㟆㟇 = 2
val 㟈㟉㟊㟋㟌㟍㟎㟏㟐㟑㟒㟓㟔㟕㟖㟗㟘㟙㟚㟛㟜㟝㟞㟟㟠㟡㟢㟣㟤㟥㟦㟧㟨㟩㟪㟫㟬㟭㟮㟯 = 2
val 㟰㟱㟲㟳㟴㟵㟶㟷㟸㟹㟺㟻㟼㟽㟾㟿㠀㠁㠂㠃㠄㠅㠆㠇㠈㠉㠊㠋㠌㠍㠎㠏㠐㠑㠒㠓㠔㠕㠖㠗 = 2
val 㠘㠙㠚㠛㠜㠝㠞㠟㠠㠡㠢㠣㠤㠥㠦㠧㠨㠩㠪㠫㠬㠭㠮㠯㠰㠱㠲㠳㠴㠵㠶㠷㠸㠹㠺㠻㠼㠽㠾㠿 = 2
val 㡀㡁㡂㡃㡄㡅㡆㡇㡈㡉㡊㡋㡌㡍㡎㡏㡐㡑㡒㡓㡔㡕㡖㡗㡘㡙㡚㡛㡜㡝㡞㡟㡠㡡㡢㡣㡤㡥㡦㡧 = 2
val 㡨㡩㡪㡫㡬㡭㡮㡯㡰㡱㡲㡳㡴㡵㡶㡷㡸㡹㡺㡻㡼㡽㡾㡿㢀㢁㢂㢃㢄㢅㢆㢇㢈㢉㢊㢋㢌㢍㢎㢏 = 2
val 㢐㢑㢒㢓㢔㢕㢖㢗㢘㢙㢚㢛㢜㢝㢞㢟㢠㢡㢢㢣㢤㢥㢦㢧㢨㢩㢪㢫㢬㢭㢮㢯㢰㢱㢲㢳㢴㢵㢶㢷 = 2
val 㢸㢹㢺㢻㢼㢽㢾㢿㣀㣁㣂㣃㣄㣅㣆㣇㣈㣉㣊㣋㣌㣍㣎㣏㣐㣑㣒㣓㣔㣕㣖㣗㣘㣙㣚㣛㣜㣝㣞㣟 = 2
val 㣠㣡㣢㣣㣤㣥㣦㣧㣨㣩㣪㣫㣬㣭㣮㣯㣰㣱㣲㣳㣴㣵㣶㣷㣸㣹㣺㣻㣼㣽㣾㣿㤀㤁㤂㤃㤄㤅㤆㤇 = 2
val 㤈㤉㤊㤋㤌㤍㤎㤏㤐㤑㤒㤓㤔㤕㤖㤗㤘㤙㤚㤛㤜㤝㤞㤟㤠㤡㤢㤣㤤㤥㤦㤧㤨㤩㤪㤫㤬㤭㤮㤯 = 2
val 㤰㤱㤲㤳㤴㤵㤶㤷㤸㤹㤺㤻㤼㤽㤾㤿㥀㥁㥂㥃㥄㥅㥆㥇㥈㥉㥊㥋㥌㥍㥎㥏㥐㥑㥒㥓㥔㥕㥖㥗 = 2
val 㥘㥙㥚㥛㥜㥝㥞㥟㥠㥡㥢㥣㥤㥥㥦㥧㥨㥩㥪㥫㥬㥭㥮㥯㥰㥱㥲㥳㥴㥵㥶㥷㥸㥹㥺㥻㥼㥽㥾㥿 = 2
val 㦀㦁㦂㦃㦄㦅㦆㦇㦈㦉㦊㦋㦌㦍㦎㦏㦐㦑㦒㦓㦔㦕㦖㦗㦘㦙㦚㦛㦜㦝㦞㦟㦠㦡㦢㦣㦤㦥㦦㦧 = 2
val 㦨㦩㦪㦫㦬㦭㦮㦯㦰㦱㦲㦳㦴㦵㦶㦷㦸㦹㦺㦻㦼㦽㦾㦿㧀㧁㧂㧃㧄㧅㧆㧇㧈㧉㧊㧋㧌㧍㧎㧏 = 2
val 㧐㧑㧒㧓㧔㧕㧖㧗㧘㧙㧚㧛㧜㧝㧞㧟㧠㧡㧢㧣㧤㧥㧦㧧㧨㧩㧪㧫㧬㧭㧮㧯㧰㧱㧲㧳㧴㧵㧶㧷 = 2
val 㧸㧹㧺㧻㧼㧽㧾㧿㨀㨁㨂㨃㨄㨅㨆㨇㨈㨉㨊㨋㨌㨍㨎㨏㨐㨑㨒㨓㨔㨕㨖㨗㨘㨙㨚㨛㨜㨝㨞㨟 = 2
val 㨠㨡㨢㨣㨤㨥㨦㨧㨨㨩㨪㨫㨬㨭㨮㨯㨰㨱㨲㨳㨴㨵㨶㨷㨸㨹㨺㨻㨼㨽㨾㨿㩀㩁㩂㩃㩄㩅㩆㩇 = 2
val 㩈㩉㩊㩋㩌㩍㩎㩏㩐㩑㩒㩓㩔㩕㩖㩗㩘㩙㩚㩛㩜㩝㩞㩟㩠㩡㩢㩣㩤㩥㩦㩧㩨㩩㩪㩫㩬㩭㩮㩯 = 2
val 㩰㩱㩲㩳㩴㩵㩶㩷㩸㩹㩺㩻㩼㩽㩾㩿㪀㪁㪂㪃㪄㪅㪆㪇㪈㪉㪊㪋㪌㪍㪎㪏㪐㪑㪒㪓㪔㪕㪖㪗 = 2
val 㪘㪙㪚㪛㪜㪝㪞㪟㪠㪡㪢㪣㪤㪥㪦㪧㪨㪩㪪㪫㪬㪭㪮㪯㪰㪱㪲㪳㪴㪵㪶㪷㪸㪹㪺㪻㪼㪽㪾㪿 = 2
val 㫀㫁㫂㫃㫄㫅㫆㫇㫈㫉㫊㫋㫌㫍㫎㫏㫐㫑㫒㫓㫔㫕㫖㫗㫘㫙㫚㫛㫜㫝㫞㫟㫠㫡㫢㫣㫤㫥㫦㫧 = 2
val 㫨㫩㫪㫫㫬㫭㫮㫯㫰㫱㫲㫳㫴㫵㫶㫷㫸㫹㫺㫻㫼㫽㫾㫿㬀㬁㬂㬃㬄㬅㬆㬇㬈㬉㬊㬋㬌㬍㬎㬏 = 2
val 㬐㬑㬒㬓㬔㬕㬖㬗㬘㬙㬚㬛㬜㬝㬞㬟㬠㬡㬢㬣㬤㬥㬦㬧㬨㬩㬪㬫㬬㬭㬮㬯㬰㬱㬲㬳㬴㬵㬶㬷 = 2
val 㬸㬹㬺㬻㬼㬽㬾㬿㭀㭁㭂㭃㭄㭅㭆㭇㭈㭉㭊㭋㭌㭍㭎㭏㭐㭑㭒㭓㭔㭕㭖㭗㭘㭙㭚㭛㭜㭝㭞㭟 = 2
val 㭠㭡㭢㭣㭤㭥㭦㭧㭨㭩㭪㭫㭬㭭㭮㭯㭰㭱㭲㭳㭴㭵㭶㭷㭸㭹㭺㭻㭼㭽㭾㭿㮀㮁㮂㮃㮄㮅㮆㮇 = 2
val 㮈㮉㮊㮋㮌㮍㮎㮏㮐㮑㮒㮓㮔㮕㮖㮗㮘㮙㮚㮛㮜㮝㮞㮟㮠㮡㮢㮣㮤㮥㮦㮧㮨㮩㮪㮫㮬㮭㮮㮯 = 2
val 㮰㮱㮲㮳㮴㮵㮶㮷㮸㮹㮺㮻㮼㮽㮾㮿㯀㯁㯂㯃㯄㯅㯆㯇㯈㯉㯊㯋㯌㯍㯎㯏㯐㯑㯒㯓㯔㯕㯖㯗 = 2
val 㯘㯙㯚㯛㯜㯝㯞㯟㯠㯡㯢㯣㯤㯥㯦㯧㯨㯩㯪㯫㯬㯭㯮㯯㯰㯱㯲㯳㯴㯵㯶㯷㯸㯹㯺㯻㯼㯽㯾㯿 = 2
val 㰀㰁㰂㰃㰄㰅㰆㰇㰈㰉㰊㰋㰌㰍㰎㰏㰐㰑㰒㰓㰔㰕㰖㰗㰘㰙㰚㰛㰜㰝㰞㰟㰠㰡㰢㰣㰤㰥㰦㰧 = 2
val 㰨㰩㰪㰫㰬㰭㰮㰯㰰㰱㰲㰳㰴㰵㰶㰷㰸㰹㰺㰻㰼㰽㰾㰿㱀㱁㱂㱃㱄㱅㱆㱇㱈㱉㱊㱋㱌㱍㱎㱏 = 2
val 㱐㱑㱒㱓㱔㱕㱖㱗㱘㱙㱚㱛㱜㱝㱞㱟㱠㱡㱢㱣㱤㱥㱦㱧㱨㱩㱪㱫㱬㱭㱮㱯㱰㱱㱲㱳㱴㱵㱶㱷 = 2
val 㱸㱹㱺㱻㱼㱽㱾㱿㲀㲁㲂㲃㲄㲅㲆㲇㲈㲉㲊㲋㲌㲍㲎㲏㲐㲑㲒㲓㲔㲕㲖㲗㲘㲙㲚㲛㲜㲝㲞㲟 = 2
val 㲠㲡㲢㲣㲤㲥㲦㲧㲨㲩㲪㲫㲬㲭㲮㲯㲰㲱㲲㲳㲴㲵㲶㲷㲸㲹㲺㲻㲼㲽㲾㲿㳀㳁㳂㳃㳄㳅㳆㳇 = 2
val 㳈㳉㳊㳋㳌㳍㳎㳏㳐㳑㳒㳓㳔㳕㳖㳗㳘㳙㳚㳛㳜㳝㳞㳟㳠㳡㳢㳣㳤㳥㳦㳧㳨㳩㳪㳫㳬㳭㳮㳯 = 2
val 㳰㳱㳲㳳㳴㳵㳶㳷㳸㳹㳺㳻㳼㳽㳾㳿㴀㴁㴂㴃㴄㴅㴆㴇㴈㴉㴊㴋㴌㴍㴎㴏㴐㴑㴒㴓㴔㴕㴖㴗 = 2
val 㴘㴙㴚㴛㴜㴝㴞㴟㴠㴡㴢㴣㴤㴥㴦㴧㴨㴩㴪㴫㴬㴭㴮㴯㴰㴱㴲㴳㴴㴵㴶㴷㴸㴹㴺㴻㴼㴽㴾㴿 = 2
val 㵀㵁㵂㵃㵄㵅㵆㵇㵈㵉㵊㵋㵌㵍㵎㵏㵐㵑㵒㵓㵔㵕㵖㵗㵘㵙㵚㵛㵜㵝㵞㵟㵠㵡㵢㵣㵤㵥㵦㵧 = 2
val 㵨㵩㵪㵫㵬㵭㵮㵯㵰㵱㵲㵳㵴㵵㵶㵷㵸㵹㵺㵻㵼㵽㵾㵿㶀㶁㶂㶃㶄㶅㶆㶇㶈㶉㶊㶋㶌㶍㶎㶏 = 2
val 㶐㶑㶒㶓㶔㶕㶖㶗㶘㶙㶚㶛㶜㶝㶞㶟㶠㶡㶢㶣㶤㶥㶦㶧㶨㶩㶪㶫㶬㶭㶮㶯㶰㶱㶲㶳㶴㶵㶶㶷 = 2
val 㶸㶹㶺㶻㶼㶽㶾㶿㷀㷁㷂㷃㷄㷅㷆㷇㷈㷉㷊㷋㷌㷍㷎㷏㷐㷑㷒㷓㷔㷕㷖㷗㷘㷙㷚㷛㷜㷝㷞㷟 = 2
val 㷠㷡㷢㷣㷤㷥㷦㷧㷨㷩㷪㷫㷬㷭㷮㷯㷰㷱㷲㷳㷴㷵㷶㷷㷸㷹㷺㷻㷼㷽㷾㷿㸀㸁㸂㸃㸄㸅㸆㸇 = 2
val 㸈㸉㸊㸋㸌㸍㸎㸏㸐㸑㸒㸓㸔㸕㸖㸗㸘㸙㸚㸛㸜㸝㸞㸟㸠㸡㸢㸣㸤㸥㸦㸧㸨㸩㸪㸫㸬㸭㸮㸯 = 2
val 㸰㸱㸲㸳㸴㸵㸶㸷㸸㸹㸺㸻㸼㸽㸾㸿㹀㹁㹂㹃㹄㹅㹆㹇㹈㹉㹊㹋㹌㹍㹎㹏㹐㹑㹒㹓㹔㹕㹖㹗 = 2
val 㹘㹙㹚㹛㹜㹝㹞㹟㹠㹡㹢㹣㹤㹥㹦㹧㹨㹩㹪㹫㹬㹭㹮㹯㹰㹱㹲㹳㹴㹵㹶㹷㹸㹹㹺㹻㹼㹽㹾㹿 = 2
*/
}
