for /L %%x in (1, 1, 50) do (
	start javaw -Xms2m -Xmx5m -jar client-1.0.jar 
)