
<dl class="tabs vertical" data-tab>
	<?php 
		for ($i = 0; $i < count($tabs); ++$i){
			$tab = $tabs[$i];
			$id = str_replace(' ','',$tab);
			if($i == 0){
				echo "<dd class=\"active\"><a href=\"#$id\">$tab</a></dd>";
			} else {
				echo "<dd><a href=\"#$id\">$tab</a></dd>";
			}
		}
	?>
</dl>

