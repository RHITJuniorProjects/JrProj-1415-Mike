
<dl class="tabs vertical" data-tab>
	<?php 
		foreach ($tabs as $tab){
			$id = str_replace(' ','',$tab);
			echo "<dd><a href=\"#$id\">$tab</a></dd>";
		}
	?>
</dl>

